package openanonymizer.core.stage;

import openanonymizer.config.Configuration;
import openanonymizer.core.validation.ConfigurationValidationException;
import openanonymizer.core.validation.ConfigurationValidator;
import org.apache.log4j.Logger;

/**
 * This class validates application configuration.
 * Application is stopped if validation failed
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class ValidationStage implements Stage {

    private final static Logger logger = Logger.getLogger(ValidationStage.class);

    @Override
    public void executeStage(Configuration configuration) {
        try {
            ConfigurationValidator.validateConfiguration(configuration);
        } catch (ConfigurationValidationException e) {
            logger.error("Configuration validation failed. Exiting.", e);
            System.exit(1);
        }
    }
}
