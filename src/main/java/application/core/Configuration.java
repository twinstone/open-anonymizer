package application.core;

import application.core.anonymizer.AnonymizationStrategy;

import java.util.Locale;
import java.util.Map;

public class Configuration {

    private final Locale locale;
    private final String dictionaryPath;
    private final AnonymizationStrategy strategy;
    private final String secret;
    private final Map<String, Object> params;

    public Configuration(Locale locale, String dictionaryPath, AnonymizationStrategy strategy, String secret, Map<String, Object> params) {
        this.locale = locale;
        this.dictionaryPath = dictionaryPath;
        this.strategy = strategy;
        this.secret = secret;
        this.params = params;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getDictionaryPath() {
        return dictionaryPath;
    }

    public AnonymizationStrategy getStrategy() {
        return strategy;
    }

    public String getSecret() {
        return secret;
    }

    public Object getParam(final String param) {
        return params.get(param);
    }
}
