package application.core;

import application.core.anonymizer.Anonymizer;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;

public class AnonymizationService {

    public EntityWrapper anonymizeEntity(EntityWrapper entity, application.config.Configuration configuration) {
        Validate.notNull(entity, "Entity must be not null.");
        Validate.notNull(configuration, "Configuration must be not null");
        entity.describeEntity().getFields().forEach(describer -> anonymizeField(entity, describer, configuration));
        return entity;
    }

    public EntityWrapper anonymizeField(EntityWrapper entity, FieldDescriber describer, application.config.Configuration configuration) {
        try {
            Class<?> anonClass = Class.forName(describer.getAnonymizationClass());
            if (Anonymizer.class.isAssignableFrom(anonClass)) {
                Anonymizer anonymizer = (Anonymizer) anonClass.getConstructor().newInstance();
                Configuration config = new Configuration(configuration.getLocale(), configuration.getDictionaryPath(), describer.getAnonymizationStrategy(), null);
                anonymizer.anonymize(entity, describer, config);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

}
