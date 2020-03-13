package application.core.anonymizer;

import application.core.Configuration;
import application.core.dict.DictionaryService;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;


public class FirstNameAnonymizer extends AbstractFieldAnonymizer implements Anonymizer {

    private static final String FILE_NAME = "first_name";
    private static final String DEFAULT_FIRST_NAME = "Bob";

    @Override
    public void anonymize(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        super.anonymize(entity, describer, configuration);
    }

    @Override
    protected void resolveRandomStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        entity.update(describer.getName(), DictionaryService.getDictionaryValue(configuration.getDictionaryPath(), FILE_NAME, configuration.getLocale()).orElse(DEFAULT_FIRST_NAME));
    }
}