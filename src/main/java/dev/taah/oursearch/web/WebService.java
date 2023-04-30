package dev.taah.oursearch.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 9:31 AM [29-04-2023]
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WebService {
    private static final String AUTH_KEY = RandomStringUtils.randomAlphanumeric(16);
    static {
        System.out.println("AUTH KEY: " + AUTH_KEY);
    }

    public static String authKey() {
        return AUTH_KEY;
    }
}
