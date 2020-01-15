package nl.com.kalah.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties of the project.
 *
 * <p> Properties are configured in the application.yml file. </p>
 *
 * @author Diego
 * @since 13/01/2020
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    /**
     * The Swagger Properties.
     *
     * @see Swagger
     */
    private final Swagger swagger;

    /**
     * Amount of stones used in the game.
     */
    private Integer amountOfStones;

    public ApplicationProperties() {
        swagger = new Swagger();
    }

    /**
     * Gets the Swagger Properties.
     *
     * @return Swagger
     */
    public Swagger getSwagger() {
        return swagger;
    }

    /**
     * Get the amount of stones.
     *
     * @return Integer
     */
    public Integer getAmountOfStones() {
        return amountOfStones;
    }

    /**
     * Set the amount of stones.
     *
     * @param amountOfStones Amount of stones.
     */
    public void setAmountOfStones(final Integer amountOfStones) {
        this.amountOfStones = amountOfStones;
    }

    /**
     * Swagger Properties.
     *
     * @author Diego
     * @since 13/01/2020
     */
    public static class Swagger {

        /**
         * The API title.
         */
        private String title;
        /**
         * The API description.
         */
        private String description;
        /**
         * The API version.
         */
        private String version;
        /**
         * The regular expression for the paths that must be analysed.
         */
        private String defaultIncludePattern;

        /**
         * Gets the API title.
         *
         * @return String
         */
        public String getTitle() {
            return title;
        }

        /**
         * Sets the API title.
         *
         * @param title API title.
         */
        public void setTitle(final String title) {
            this.title = title;
        }

        /**
         * Gets the API description.
         *
         * @return String
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the API description.
         *
         * @param description API description.
         */
        public void setDescription(final String description) {
            this.description = description;
        }

        /**
         * Gets the API version.
         *
         * @return String
         */
        public String getVersion() {
            return version;
        }

        /**
         * Sets the API version.
         *
         * @param version API version.
         */
        public void setVersion(final String version) {
            this.version = version;
        }

        /**
         * Gets the regular expression for the paths that must be analysed.
         *
         * @return String
         */
        public String getDefaultIncludePattern() {
            return defaultIncludePattern;
        }

        /**
         * Sets the regular expression for the paths that must be analysed.
         */
        public void setDefaultIncludePattern(final String defaultIncludePattern) {
            this.defaultIncludePattern = defaultIncludePattern;
        }
    }
}
