package application.core.anonymizer;

import application.core.Configuration;
import application.core.dict.MultiDictionaryService;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Optional;

public class MultiDictionaryAndPatternBasedAnonymizer extends AbstractFieldAnonymizer implements Anonymizer {

    private static final String PATTERN = "pattern";
    private static final String DICTIONARIES = "dictionaries";
    private static final String PATTERNS = "patterns";
    private static final String DEFAULT_VALUE = "default";

    @Override
    protected void resolveRandomStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        String result = MultiDictionaryService.generate(getPattern(configuration),
                configuration.getDictionaryPath(),
                getDictionaryNames(configuration),
                getPatterns(configuration),
                configuration.getLocale());
        if (StringUtils.isEmpty(result)) {
            result = getDefaultValue(configuration, describer);
        }
        entity.update(describer.getName(), result);
    }

    private String getPattern(Configuration configuration) {
        Validate.notNull(configuration.getParam(PATTERN), "Configuration param pattern must be not null.");
        return String.valueOf(configuration.getParam(PATTERN));
    }

    private String[] getDictionaryNames(Configuration configuration) {
        return (String[]) Optional.ofNullable(configuration.getParam(DICTIONARIES)).orElse(null);
    }

    private String[] getPatterns(Configuration configuration) {
        return (String[]) Optional.ofNullable(configuration.getParam(PATTERNS)).orElse(null);
    }

    private String getDefaultValue(Configuration configuration, FieldDescriber describer) {
        if (configuration.getParam(DEFAULT_VALUE) == null) return describer.getDefaultValue();
        return String.valueOf(configuration.getParam(DEFAULT_VALUE));
    }
}
