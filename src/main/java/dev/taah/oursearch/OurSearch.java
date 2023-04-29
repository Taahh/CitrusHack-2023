package dev.taah.oursearch;

import dev.taah.oursearch.web.WebService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 9:26 AM [29-04-2023]
 */
public class OurSearch {

    public static void main(String[] args) {
        SpringApplication.run(WebService.class, args);
    }
}
