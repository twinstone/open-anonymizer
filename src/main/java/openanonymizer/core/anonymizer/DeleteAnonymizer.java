package openanonymizer.core.anonymizer;

import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;

/**
 * Removes field from wrapper.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class DeleteAnonymizer implements Anonymizer {
    @Override
    public void anonymize(EntityWrapper wrapper, FieldDescriber describer, Configuration configuration) {
        if (describer.isUnique() || !describer.allowsNull()) {
            throw new IllegalArgumentException("Could not delete field that is unique or does not allow null value.");
        }
        wrapper.delete(describer.getName());
    }
}
