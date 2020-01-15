package nl.com.kalah.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import nl.com.kalah.api.config.ApplicationProperties;

/**
 * Main class.
 *
 * @author Diego
 * @since 13/01/2020
 */
@EnableConfigurationProperties(ApplicationProperties.class)
@SpringBootApplication(scanBasePackages = "nl.com.kalah.api")
public class KalahApplication {

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(final String... args) {
        SpringApplication.run(KalahApplication.class, args);
    }

}

