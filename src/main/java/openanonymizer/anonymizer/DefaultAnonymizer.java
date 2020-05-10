package openanonymizer.anonymizer;

import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;

/**
 * Replaces field value with default value.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public abstract class DefaultAnonymizer<T> implements Anonymizer {
    @Override
    public void anonymize(EntityWrapper wrapper, FieldDescriber describer, AnonymizationConfiguration configuration) {
        if (describer.isUnique())
            throw new IllegalStateException("Could not set default value into field that is unique.");
        wrapper.update(describer.getName(), getDefaultValue(describer.getDefaultValue(), describer.getDefaultParams()));
    }

    abstract T getDefaultValue(String input, String[] params);
}
