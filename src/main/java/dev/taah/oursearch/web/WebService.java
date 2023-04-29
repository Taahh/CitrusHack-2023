package dev.taah.oursearch.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 9:31 AM [29-04-2023]
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WebService {
}
