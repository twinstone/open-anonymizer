package openanonymizer.config.loader;

import openanonymizer.config.Configuration;

/**
 * This interface has only one method. It allows to load configuration from file in specified format.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public interface ConfigurationLoader {

    /**
     * Reads configuration file and return configuration class.
     *
     * @param filePath path to file in storage
     * @return application configuration wrapped by {@link Configuration} class
     */
    Configuration readConfiguration(String filePath);
}
