package application.core.anonymizer;

import application.core.Configuration;
import application.core.dict.DictionaryService;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;

import java.util.UUID;

public class EmailAnonymizer extends AbstractFieldAnonymizer implements Anonymizer {

    private static final String DOMAINS = "domains";
    private static final String DEFAULT_DOMAIN = "default.com";
    private static final String EMAIL = "%s@%s";

    @Override
    public void anonymize(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        super.anonymize(entity, describer, configuration);
    }

    @Override
    protected void resolveRandomStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        String domain = DictionaryService.getDictionaryValue(configuration.getDictionaryPath(), DOMAINS).orElse(DEFAULT_DOMAIN);
        String uuid = UUID.randomUUID().toString();
        entity.update(describer.getName(), String.format(EMAIL, uuid, domain));
    }
}
