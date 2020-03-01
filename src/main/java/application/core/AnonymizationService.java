package application.core;

import application.core.anonymizer.Anonymizer;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;

import java.lang.reflect.InvocationTargetException;

public class AnonymizationService {

    public EntityWrapper anonymizeEntity(EntityWrapper entity) {
        entity.describeEntity().getFields().forEach(describer -> anonymizeField(entity, describer));
        return entity;
    }

    public EntityWrapper anonymizeField(EntityWrapper entity, FieldDescriber describer) {
        try {
            Class<?> anonClass = Class.forName(describer.getAnonymizationClass());
            if (Anonymizer.class.isAssignableFrom(anonClass)) {
                Anonymizer anonymizer = (Anonymizer) anonClass.getConstructor().newInstance();
                Configuration configuration = new Configuration(describer.getAnonymizationStrategy(), null);
                anonymizer.anonymize(entity, describer, configuration);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

}
