package application.core.anonymizer;

import application.core.Configuration;
import application.core.hash.HashService;
import application.model.FieldType;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;

public abstract class AbstractFieldAnonymizer {

    protected void resolveDefaultStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        if (describer.isUnique()) {
            throw new IllegalArgumentException("Could not set default value to field that is unique.");
        }
        //todo another resolving default strategy
        entity.update(describer.getName(), describer.getDefaultValue());
    }

    protected void resolveDeleteStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        if (describer.isUnique() || !describer.allowsNull()) {
            throw new IllegalArgumentException("Could not delete field that is unique or does not allow null value.");
        }
        entity.delete(describer.getName());
    }

    protected void resolveRandomStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        throw new UnsupportedOperationException();
    }

    protected void resolveHashStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        if (FieldType.STRING.equals(describer.getType())) {
            String input = entity.getValue(describer.getName()).toString();
            entity.update(describer.getName(), HashService.generateHash(input, configuration.getSecret()));
        }
    }

    public void anonymize(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        Validate.notNull(entity);
        Validate.notNull(describer);
        Validate.notNull(configuration);
        switch (configuration.getStrategy()) {
            case DEFAULT:
                resolveDefaultStrategy(entity, describer, configuration);
                break;
            case DELETE:
                resolveDeleteStrategy(entity, describer, configuration);
                break;
            case RANDOM:
                resolveRandomStrategy(entity, describer, configuration);
                break;
            case HASH:
                resolveHashStrategy(entity, describer, configuration);
                break;
                default:
                    throw new IllegalArgumentException("No such strategy: " + configuration.getStrategy());
        }
    }
}
