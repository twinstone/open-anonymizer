package openanonymizer.service;

import openanonymizer.core.anonymizer.Anonymizer;
import openanonymizer.core.anonymizer.Configuration;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;

public class TestAnonymizer implements Anonymizer {
    @Override
    public void anonymize(EntityWrapper wrapper, FieldDescriber describer, Configuration configuration) {
        wrapper.update(describer.getName(), "anonymized");
    }
}