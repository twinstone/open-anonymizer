package application.core.validation;

import application.config.Configuration;
import application.model.describer.EntityDescriber;
import application.model.describer.FieldDescriber;
import application.model.describer.RelationFieldDescriber;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

public class ConfigurationValidator {

    private static final Logger logger = Logger.getLogger(ConfigurationValidator.class);

    public static void validateFieldsAndRelations(Configuration configuration) {
        logger.info("Validating configuration started.");
        Validate.notNull(configuration.getInputSource(), "");
        Validate.notNull(configuration.getOutputSource(), "");
        Validate.notNull(configuration.getLevel(), "");
        Validate.notEmpty(configuration.getEntities(), "");
        try {
            logger.info("Validating ");
            for (final EntityDescriber entity : configuration.getEntities()) {
                Validate.notEmpty(entity.getName(), "");
                Validate.notEmpty(entity.getSource(), "");
                Validate.notEmpty(entity.getFields(), "");
                for (final FieldDescriber field : entity.getFields()) {
                    Validate.notEmpty(field.getName());
                    Validate.notEmpty(field.getConfiguration().getAnonymizationClass());
                    Validate.notNull(field.getConfiguration().getAnonymizationStrategy());
                    //todo validation
                }
                if (entity.getRelationFields() != null) {
                    for (final RelationFieldDescriber field : entity.getRelationFields()) {
                        Validate.notEmpty(field.getName(), "Name must be not empty in relation field.");
                        Validate.notEmpty(field.getTargetSource(), "Target source must be not empty in relation field.");
                        Validate.notNull(field.getRelationType(), "Relation type must be not null in relation field.");
                        if (configuration.getEntities().stream().noneMatch(e -> field.getTargetSource().equals(e.getSource()))) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ConfigurationValidationException(e);
        }
        logger.info("Configuration structure valid.");
    }

    public static void validateEntities(Configuration configuration) {
        //todo implementation
    }
}
