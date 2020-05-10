package openanonymizer.anonymizer;

import openanonymizer.core.dict.MultiDictionaryService;
import openanonymizer.core.transformer.Transformer;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Multi pattern based anonymizer. Replaces field value with randomly generated value.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class MultiDictionaryAndPatternBasedAnonymizer extends TransformerBasedAnonymizer implements Anonymizer {

    private static final String PATTERN = "pattern";
    private static final String DICTIONARIES = "dictionaries";
    private static final String PATTERNS = "patterns";
    private static final String DEFAULT_VALUE = "default";

    @Override
    public void anonymize(EntityWrapper wrapper, FieldDescriber describer, AnonymizationConfiguration configuration) throws Exception {
        String result = MultiDictionaryService.generate(getPattern(configuration),
                configuration.getDictionaryPath(),
                getDictionaryNames(configuration).toArray(new String[0]),
                getPatterns(configuration).toArray(new String[0]),
                configuration.getLocale());
        if (StringUtils.isEmpty(result)) {
            result = getDefaultValue(configuration, describer);
        }
        Transformer transformer = getTransformer(configuration);
        wrapper.update(describer.getName(), transformer.transform(result));
    }

    private String getPattern(AnonymizationConfiguration configuration) {
        Validate.notNull(configuration.getParam(PATTERN), "Configuration param pattern must be not null.");
        return String.valueOf(configuration.getParam(PATTERN));
    }

    @SuppressWarnings("unchecked")
    private ArrayList<String> getDictionaryNames(AnonymizationConfiguration configuration) {
        return (ArrayList<String>) Optional.ofNullable(configuration.getParam(DICTIONARIES)).orElse(null);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<String> getPatterns(AnonymizationConfiguration configuration) {
        return (ArrayList<String>) Optional.ofNullable(configuration.getParam(PATTERNS)).orElse(null);
    }

    private String getDefaultValue(AnonymizationConfiguration configuration, FieldDescriber describer) {
        if (configuration.getParam(DEFAULT_VALUE) == null) return describer.getDefaultValue();
        return String.valueOf(configuration.getParam(DEFAULT_VALUE));
    }
}
