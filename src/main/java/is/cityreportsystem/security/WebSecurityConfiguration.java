package is.cityreportsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
//import org.unibl.etf.is.am.security.models.AuthorizationRules;
//import org.unibl.etf.is.am.security.models.Rule;
import is.cityreportsystem.services.JwtUserDetailsService;

@EnableWebSecurity   // includes our custom security configuration into spring security configuration
@Configuration       // marks our class as configuration class
public class WebSecurityConfiguration{

    // service that enables retrieving user data based on specified username
    private final JwtUserDetailsService jwtUserDetailsService;
    // authorization filter that validates received requests
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    // takes care of requests which failed validation
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    public WebSecurityConfiguration(JwtUserDetailsService jwtUserDetailsService, JwtAuthorizationFilter jwtAuthorizationFilter, JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

     //interface declaring authenticate()  method, which accepts or rejects request depending on success of authentication
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    // bean that provides password hashing and encoding
    @Bean
    public PasswordEncoder passwordEncoder() {      //service that provides password encryption and encoding
        return new BCryptPasswordEncoder();
    }
    //declaring rules for accessing api
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//                .and().authorizeHttpRequests()
//                .requestMatchers(HttpMethod.GET, "/login").permitAll() //enables unauthorized requests
//                .requestMatchers(HttpMethod.POST, "/signup").permitAll()
//                .requestMatchers(HttpMethod.GET, "/cityServices").permitAll()
//                .requestMatchers(HttpMethod.GET, "/events/active/images/*").permitAll()
//                .requestMatchers(HttpMethod.GET, "/events/active").permitAll()
//                .requestMatchers(HttpMethod.GET, "/events/types").permitAll()
//                .requestMatchers(HttpMethod.GET, "/reports/types").permitAll()
//                .requestMatchers(HttpMethod.GET, "/reports/states").permitAll()
//                .requestMatchers(HttpMethod.GET, "/reports/images/*").permitAll()
//                .requestMatchers(HttpMethod.GET, "/statistics/**").hasAuthority("CITY_MANAGER")
//                .anyRequest().authenticated();

//                .requestMatchers(HttpMethod.GET, "/events/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/statistics/**").permitAll()
//                .requestMatchers(HttpMethod.DELETE, "/events/**").permitAll()
//                .requestMatchers(HttpMethod.PUT, "/events/**").permitAll()
//                .requestMatchers(HttpMethod.POST, "/events/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/reports/**").permitAll()
//                .requestMatchers(HttpMethod.PUT, "/reports/**").permitAll()
//                .requestMatchers(HttpMethod.POST, "/reports").permitAll()
//                .requestMatchers(HttpMethod.PUT, "/reports/*").permitAll()
//                .requestMatchers(HttpMethod.PUT, "/reports/additionalInfo/*").permitAll()
//                .requestMatchers(HttpMethod.GET, "/reports/author/*").permitAll()
//                .requestMatchers(HttpMethod.POST, "/reports/images/upload").permitAll()
//                .requestMatchers(HttpMethod.DELETE, "/reports/images/*").permitAll()

        http=createAuthorizationRules(http);
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    private HttpSecurity createAuthorizationRules(HttpSecurity http) throws Exception {
        AuthorizationRules authorizationRules = new ObjectMapper().readValue(new ClassPathResource("rules.json").getInputStream(), AuthorizationRules.class);
       AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry=http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/login").permitAll() //enables unauthorized requests
                .requestMatchers(HttpMethod.POST, "/signup").permitAll()
                .requestMatchers(HttpMethod.GET, "/cityServices").permitAll()
                .requestMatchers(HttpMethod.GET, "/events/active/images/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/events/active").permitAll()
                .requestMatchers(HttpMethod.GET, "/events/types").permitAll()
                .requestMatchers(HttpMethod.GET, "/reports/types").permitAll()
                .requestMatchers(HttpMethod.GET, "/reports/states").permitAll()
                .requestMatchers(HttpMethod.GET, "/reports/images/*").permitAll();
        for (Rule rule : authorizationRules.getRules()) {
            System.out.println(rule.getPattern());
            if (rule.getMethods().isEmpty())
                registry=registry.requestMatchers(rule.getPattern()).hasAnyAuthority(rule.getRoles().toArray(String[]::new));
            else for (String method : rule.getMethods()) {
                registry=registry.requestMatchers(HttpMethod.valueOf(method), rule.getPattern()).hasAnyAuthority(rule.getRoles().toArray(String[]::new));
            }
        }
        return registry.anyRequest().authenticated().and();
    }

    // cors filter - we shouldn't allow all methods origins and headers...
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    //changes role prefix that is added automaticaly
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

}
