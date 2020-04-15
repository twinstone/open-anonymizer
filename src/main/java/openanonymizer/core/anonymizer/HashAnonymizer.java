package openanonymizer.core.anonymizer;

import openanonymizer.core.hash.HashService;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;

/**
 * Replaces field value with a hash generated from input,
 * using secret specified in {@link Configuration}.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class HashAnonymizer implements Anonymizer {
    @Override
    public void anonymize(EntityWrapper wrapper, FieldDescriber describer, Configuration configuration) {
        String input = wrapper.getValue(describer.getName()).toString();
        wrapper.update(describer.getName(), HashService.generateHash(input, configuration.getSecret()));
    }
}
