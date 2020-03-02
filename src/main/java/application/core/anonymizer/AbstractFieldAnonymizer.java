package application.core.anonymizer;

import application.core.Configuration;
import application.core.hash.HashService;
import application.model.FieldType;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;

public abstract class AbstractFieldAnonymizer {

    protected void resolveDefaultStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        if (describer.isUnique()) {
            throw new IllegalArgumentException("Could not set default value to field that is unique.");
        }
        entity.update(describer.getName(), describer.getDefaultValue());
    }

    protected void resolveDeleteStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        if (describer.isUnique() || !describer.allowsNull()) {
            throw new IllegalArgumentException("Could not delete field that is unique or does not allow null value.");
        }
        entity.delete(describer.getName());
    }

    protected void resolveRandomStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {}

    protected void resolveHashStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        if (FieldType.STRING.equals(describer.getType())) {
            String input = entity.getValue(describer.getName()).toString();
            entity.update(describer.getName(), HashService.generateHash(input, configuration.getSecret()));
        }
    }

    protected void anonymize(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
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
