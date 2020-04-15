package openanonymizer.core.anonymizer;

import openanonymizer.core.dict.DictionaryService;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;

/**
 * Dictionary based anonymizer. Replaces field value with value from dictionary.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 * */
public class DictionaryBasedAnonymizer implements Anonymizer {

    private static final String DICTIONARY = "dictionary";
    private static final String DEFAULT_VALUE = "default";

    @Override
    public void anonymize(EntityWrapper wrapper, FieldDescriber describer, Configuration configuration) {
        wrapper.update(describer.getName(), DictionaryService.getDictionaryValue(configuration.getDictionaryPath(), getDictionaryName(configuration), configuration.getLocale())
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
