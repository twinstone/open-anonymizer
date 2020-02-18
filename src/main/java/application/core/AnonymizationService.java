package application.core;

import application.core.anonymizer.Anonymizer;
import application.model.describer.EntityDescriber;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;

import java.lang.reflect.InvocationTargetException;

public class AnonymizationService {

    public EntityWrapper anonymizeEntity(EntityWrapper entity, EntityDescriber describer, final Configuration configuration) {
        describer.getFields().forEach(fieldDescriber -> anonymizeField(entity, fieldDescriber, configuration));
        return entity;
    }

    public EntityWrapper anonymizeField(EntityWrapper entity, FieldDescriber describer, final Configuration configuration) {
        try {
            Class<?> anonymizerClass = Class.forName(describer.getAnonymizationClass());
            if(anonymizerClass.isAssignableFrom(Anonymizer.class)) {
                Anonymizer anonymizer = (Anonymizer) anonymizerClass.getConstructor().newInstance();
                anonymizer.anonymize(entity, describer, );
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
