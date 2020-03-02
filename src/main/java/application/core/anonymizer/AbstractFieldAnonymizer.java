package application.core.anonymizer;

import application.core.Configuration;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;

public abstract class AbstractFieldAnonymizer {

    protected void resolveDefaultStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {}

    protected void resolveDeleteStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {}

    protected void resolveRandomStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {}

    protected void resolveHashStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {}

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
