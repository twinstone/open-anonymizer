package application.core.anonymizer;

import application.core.Configuration;
import application.core.dict.DictionaryService;
import application.core.hash.HashService;
import application.model.FieldType;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;


public class FirstNameAnonymizer extends AbstractFieldAnonymizer implements Anonymizer {
    @Override
    public void anonymize(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        super.anonymize(entity, describer, configuration);
    }

    @Override
    protected void resolveDefaultStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        if (describer.isUnique()) {
            throw new IllegalArgumentException("Could not set default value to field that is unique.");
        }
        entity.update(describer.getName(), describer.getDefaultValue());
    }

    @Override
    protected void resolveDeleteStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        if (describer.isUnique() || !describer.allowsNull()) {
            throw new IllegalArgumentException("Could not delete field that is unique or does not allow null value.");
        }
        entity.delete(describer.getName());
    }

    @Override
    protected void resolveRandomStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        entity.update(describer.getName(), DictionaryService.getDictionaryValue(configuration.getDictionaryPath(), describer.getName(), configuration.getLocale()));
    }

    @Override
    protected void resolveHashStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        if(FieldType.STRING.equals(describer.getType())) {
            String input = entity.getValue(describer.getName()).toString();
            entity.update(describer.getName(), HashService.generateHash(input, configuration.getSecret()));
        }
    }
}
