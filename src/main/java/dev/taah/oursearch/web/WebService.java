package dev.taah.oursearch.web;

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
}