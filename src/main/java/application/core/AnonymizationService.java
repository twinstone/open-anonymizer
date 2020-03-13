package application.core;

import application.config.Aliases;
import application.core.anonymizer.Anonymizer;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;

import java.util.Locale;

public class AnonymizationService {

    public static EntityWrapper anonymizeEntity(EntityWrapper entity, Locale locale, String dictPath) {
        Validate.notNull(entity, "Entity must be not null.");
        Validate.notNull(locale, "Locale must be not null");
        Validate.notNull(dictPath, "Dictionaries path must be not null");
        entity.describeEntity().getFields().forEach(describer -> anonymizeField(entity, describer, locale, dictPath));
        return entity;
    }

    private static EntityWrapper anonymizeField(EntityWrapper entity, FieldDescriber describer, Locale locale, String dictPath) {
        try {
            Class<?> anonClass = Class.forName(Aliases.tryFindAnonymizationClass(describer.getAnonymizationClass()));
            if (Anonymizer.class.isAssignableFrom(anonClass)) {
                Anonymizer anonymizer = (Anonymizer) anonClass.getConstructor().newInstance();
                Configuration config = new Configuration(locale, dictPath, describer.getAnonymizationStrategy(), null);
                anonymizer.anonymize(entity, describer, config);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

}
