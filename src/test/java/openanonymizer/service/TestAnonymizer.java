package openanonymizer.service;

import openanonymizer.anonymizer.AnonymizationConfiguration;
import openanonymizer.anonymizer.Anonymizer;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;

/**
 * Test anonymization class. No functionality expected.
 */
public class TestAnonymizer implements Anonymizer {
    @Override
    public void anonymize(EntityWrapper wrapper, FieldDescriber describer, AnonymizationConfiguration configuration) {
        wrapper.update(describer.getName(), "anonymized");
    }
}