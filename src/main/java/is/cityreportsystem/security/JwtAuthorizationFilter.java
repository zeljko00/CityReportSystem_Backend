package is.cityreportsystem.security;

import io.jsonwebtoken.ExpiredJwtException;
import is.cityreportsystem.services.JwtUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
// intercepts and checks each arrived request before calling controller's method
// if request fails validation it is going to be rejected
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JwtUserDetailsService jwtUserDetailsService;
    private JwtUtil jwtUtil;

    public JwtAuthorizationFilter(JwtUserDetailsService jwtUserDetailsService, JwtUtil jwtUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("Filter hit!");
        //getting header from request header
        String tokenHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;
        //checks whether request contains jwt
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            System.out.println("Token: "+tokenHeader);
            token = tokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");              //logovati
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");                //logovati
            } catch(Exception e){
                //logovati
            }
        }
        else
            System.out.println("No token!");
        if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Hit!");
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken
                        authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new
                        WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("Token valid!");
            }
        }
        filterChain.doFilter(request, response);
    }
}
