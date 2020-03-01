package application.core.anonymizer;

import application.core.Configuration;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;

public interface Anonymizer {
    void anonymize(EntityWrapper entity, FieldDescriber describer, Configuration configuration);
}
