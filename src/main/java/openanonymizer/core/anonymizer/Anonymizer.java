package openanonymizer.core.anonymizer;

import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;

/**
 * This interface provides method for applying anonymization process on entity.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public interface Anonymizer {

    /**
     * Applies anonymization process on field specified by {@link FieldDescriber} class in entity.
     *
     * @param wrapper       entity container
     * @param describer     field structure
     * @param configuration anonymization process configuration
     * @throws Exception if anonymization process failed
     */
    void anonymize(EntityWrapper wrapper, FieldDescriber describer, Configuration configuration) throws Exception;
}
