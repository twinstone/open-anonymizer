package application.core;

import application.config.Aliases;
import application.core.anonymizer.Anonymizer;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.util.Locale;

public class AnonymizationService {

    private static final Logger logger = Logger.getLogger(AnonymizationService.class);

    public static EntityWrapper anonymizeEntity(EntityWrapper entity, Locale locale, String dictPath, String secret) {
        Validate.notNull(entity, "Entity must be not null.");
        Validate.notNull(locale, "Locale must be not null");
        Validate.notNull(dictPath, "Dictionaries path must be not null");
        entity.describeEntity().getFields().forEach(describer -> anonymizeField(entity, describer, locale, dictPath, secret));
        return entity;
    }

    private static EntityWrapper anonymizeField(EntityWrapper entity, FieldDescriber describer, Locale locale, String dictPath, String secret) {
        try {
            Class<?> anonClass = Class.forName(Aliases.tryFindAnonymizationClass(describer.getConfiguration().getAnonymizationClass()));
            if (Anonymizer.class.isAssignableFrom(anonClass)) {
                Anonymizer anonymizer = (Anonymizer) anonClass.getConstructor().newInstance();
                Configuration config = new Configuration(locale, dictPath,
                        describer.getConfiguration().getAnonymizationStrategy(),
                        describer.getConfiguration().getSecret() == null ? secret : describer.getConfiguration().getSecret(),
                        describer.getConfiguration().getParams());
                anonymizer.anonymize(entity, describer, config);
            }
        } catch (Exception e) {
            //todo logger info and error
            logger.error(e);
        }
        return entity;
    }

}
