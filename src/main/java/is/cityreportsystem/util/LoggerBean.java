package is.cityreportsystem.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerBean {
    private Logger logger=LoggerFactory.getLogger("error");

    public void logError(Exception e){
        logger.warn(e.toString());
    }
@Bean
    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
