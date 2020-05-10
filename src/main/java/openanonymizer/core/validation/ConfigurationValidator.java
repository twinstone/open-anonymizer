package openanonymizer.core.validation;

import openanonymizer.config.Aliases;
import openanonymizer.config.ApplicationConfiguration;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.describer.RelationEntityDescriber;
import openanonymizer.model.describer.RelationFieldDescriber;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

/**
 * This class validates application configuration {@link ApplicationConfiguration}.
 * <p>
 * Use static methods of this class. Do not use constructor.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class ConfigurationValidator {

    private static final Logger logger = Logger.getLogger(ConfigurationValidator.class);

    /**
     * Validate required fields in configuration.
     *
     * @param configuration application configuration
     * @throws ConfigurationValidationException if validation failed
     */
    public static void validateConfiguration(ApplicationConfiguration configuration) throws ConfigurationValidationException {
        logger.info("Validating configuration started.");
        try {
            Validate.notNull(configuration.getInputSource(), "Input source must be not null.");
            Validate.notNull(configuration.getOutputSource(), "Output source must be not null.");
            Validate.notNull(configuration.getLevel(), "Level must be not null.");
            Validate.notNull(configuration.getThreads(), "Threads must be not null.");
            Validate.notNull(configuration.getPageSize(), "Page size must be not null.");
            Validate.notEmpty(configuration.getEntities(), "Entities must be not null or empty.");
            if (configuration.getStages() != null) {
                for (final String stage : configuration.getStages()) {
                    Class.forName(stage);
                }
            }
            logger.info("Validating entities.");
            for (final EntityDescriber entity : configuration.getEntities()) {
                Validate.notEmpty(entity.getName(), "Name must be not null.");
                Validate.notEmpty(entity.getSource(), "Source must be not null.");
                Validate.notEmpty(entity.getFields(), "Fields must be not null or empty.");
                if (StringUtils.isNotEmpty(entity.getId()) && !entity.containsField(entity.getId())) {
                    throw new IllegalArgumentException("Fields does not contain id field.");
                }
                logger.info(String.format("Validating fields of [%s].", entity.getName()));
                for (final FieldDescriber field : entity.getFields()) {
                    Validate.notEmpty(field.getName());
                    if (field.getConfiguration() != null && StringUtils.isNotEmpty(field.getConfiguration().getAnonymizationClass())) {
                        Class.forName(Aliases.tryFindAnonymizationClass(field.getConfiguration().getAnonymizationClass()));
                    }
                }
                logger.info(String.format("Validating relation fields of [%s].", entity.getName()));
                if (entity.getRelationFields() != null) {
                    for (final RelationFieldDescriber field : entity.getRelationFields()) {
                        Validate.notEmpty(field.getName(), "Name must be not empty in relation field.");
                        Validate.notEmpty(field.getTargetSource(), "Target source must be not empty in relation field.");
                        Validate.notNull(field.getRelationType(), "Relation type must be not null in relation field.");
                        if (configuration.getEntities().stream().noneMatch(e -> field.getTargetSource().equals(e.getSource()))) {
                            throw new IllegalArgumentException("Parent source is not valid entity.");
                        }
                    }
                }
            }
            if (configuration.getRelationEntities() != null) {
                for (final RelationEntityDescriber relationEntity : configuration.getRelationEntities()) {
                    Validate.notEmpty(relationEntity.getName(), "Name must be not null.");
                    Validate.notEmpty(relationEntity.getSource(), "Source must be not null.");
                    Validate.notNull(relationEntity.getRight(), "Right field must be not null.");
                    Validate.notNull(relationEntity.getLeft(), "Left field must be not null .");
                    if (configuration.getEntities().stream().noneMatch(e -> relationEntity.getRight().getParentSource().equals(e.getSource()))) {
                        throw new IllegalArgumentException("Target source is not valid entity.");
                    }
                    if (configuration.getEntities().stream().noneMatch(e -> relationEntity.getLeft().getParentSource().equals(e.getSource()))) {
                        throw new IllegalArgumentException("Parent source is not valid entity.");
                    }
                }
            }
        } catch (IllegalArgumentException | NullPointerException | ClassNotFoundException e) {
            throw new ConfigurationValidationException(e);
        }
        logger.info("Configuration structure valid.");
    }

    private ConfigurationValidator() {
        throw new IllegalStateException();
    }
}
