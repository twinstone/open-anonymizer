package openanonymizer.core.anonymizer;

import openanonymizer.config.Aliases;
import openanonymizer.config.Configuration.ValidationLevel;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.util.Locale;


/**
 * This class allows to perform anonymization process on entity.
 * It applies anonymization on each property field that entity contains.
 * Relation fields are ignored.
 * <p>
 * Anonymization classes are created during run time and must implement interface {@link Anonymizer}.
 * Predefined anonymization classes are declared in {@link Aliases}
 * <p>
 * Use static methods of this class. Do not use constructor.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class AnonymizationService {

    private static final Logger logger = Logger.getLogger(AnonymizationService.class);

    /**
     * Applies anonymization process on single entity.
     * Parameters entity and validation level must be not null.
     *
     * @param entity          wrapper for entity
     * @param locale          locale that will be used in {@link DictionaryBasedAnonymizer} and {@link MultiDictionaryAndPatternBasedAnonymizer}
     * @param dictPath        path to dictionary files
     * @param secret          char sequence that can used for hashing as in {@link HashAnonymizer}
     * @param validationLevel define how application should react if anonymization process failed
     * @param loader          JVM loader for classes
     * @return {@link EntityWrapper}
     * @throws AnonymizationException if anonymization process failed
     */
    public static EntityWrapper anonymizeEntity(EntityWrapper entity, Locale locale, String dictPath, String secret, ValidationLevel validationLevel, ClassLoader loader)
            throws AnonymizationException {
        Validate.notNull(entity, "Entity must be not null.");
        Validate.notNull(validationLevel, "Validation level must be not null");
        for (final FieldDescriber describer : entity.describeEntity().getFields()) {
            if (describer.getConfiguration() == null) {
                logger.info(String.format("Anonymization configuration for field [%s] in entity [%s] is not present. Skipping.", describer.getName(), entity.describeEntity().getName()));
                continue;
            }
            anonymizeField(entity, describer, locale, dictPath, secret, validationLevel, loader);
        }
        return entity;
    }

    /**
     * Applies anonymization process on specific field in entity.
     *
     * @param entity          wrapper for entity
     * @param describer       describe specific field in entity
     * @param locale          locale that will be used in {@link DictionaryBasedAnonymizer} and {@link MultiDictionaryAndPatternBasedAnonymizer}
     * @param dictPath        path to dictionary files
     * @param secret          char sequence that can used for hashing as in {@link HashAnonymizer}
     * @param validationLevel define how application should react if anonymization process failed
     * @param loader          JVM loader for classes
     * @throws AnonymizationException if anonymization process failed
     */
    private static void anonymizeField(EntityWrapper entity, FieldDescriber describer, Locale locale, String dictPath, String secret, ValidationLevel validationLevel, ClassLoader loader)
            throws AnonymizationException {
        try {
            Class<?> anonClass;
            if (loader == null) {
                anonClass = Class.forName(Aliases.tryFindAnonymizationClass(describer.getConfiguration().getAnonymizationClass()));
            } else {
                anonClass = Class.forName(describer.getConfiguration().getAnonymizationClass(), true, loader);
            }
            if (Anonymizer.class.isAssignableFrom(anonClass)) {
                Anonymizer anonymizer = (Anonymizer) anonClass.getConstructor().newInstance();
                Configuration config = new Configuration(locale, dictPath,
                        describer.getConfiguration().getSecret() == null ? secret : describer.getConfiguration().getSecret(),
                        describer.getConfiguration().getParams());
                anonymizer.anonymize(entity, describer, config);
            } else {
                throw new IllegalArgumentException(String.format("Class [%s] does not implements interface application.core.anonymizer.Anonymizer.",
                        describer.getConfiguration().getAnonymizationClass()));
            }
        } catch (Exception e) {
            switch (validationLevel) {
                case INFO:
                    logger.info(String.format("Exception executing anonymization on field [%s].", describer.getName()));
                    break;
                case WARN:
                    logger.warn(String.format("Exception executing anonymization on field [%s].", describer.getName()), e);
                    break;
                case ERROR:
                    throw new AnonymizationException(e);
            }
        }
    }

    private AnonymizationService() {
        throw new IllegalStateException();
    }
}
