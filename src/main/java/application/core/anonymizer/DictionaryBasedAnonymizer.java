package application.core.anonymizer;

import application.core.Configuration;
import application.core.dict.DictionaryService;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;

/**
 *
 */
public class DictionaryBasedAnonymizer extends AbstractFieldAnonymizer implements Anonymizer {

    private static final String DICTIONARY = "dictionary";
    private static final String DEFAULT_VALUE = "default";

    @Override
    protected void resolveRandomStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        entity.update(describer.getName(), DictionaryService.getDictionaryValue(configuration.getDictionaryPath(), getDictionaryName(configuration), configuration.getLocale())
                .orElse(getDefaultValue(configuration, describer)));
    }

    private String getDictionaryName(Configuration configuration) {
        Validate.notNull(configuration.getParam(DICTIONARY), "Configuration param dictionary must be not null.");
        return String.valueOf(configuration.getParam(DICTIONARY));
    }

    private String getDefaultValue(Configuration configuration, FieldDescriber describer) {
        if (configuration.getParam(DEFAULT_VALUE) == null) return describer.getDefaultValue();
        return String.valueOf(configuration.getParam(DEFAULT_VALUE));
    }
}
