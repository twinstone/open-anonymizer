package openanonymizer.core.anonymizer;

import java.util.Locale;
import java.util.Map;

/**
 * Anonymization process configuration file.
 * All fields are optional.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class Configuration {

    private final Locale locale;
    private final String dictionaryPath;
    private final String secret;
    private final Map<String, Object> params;

    public Configuration(Locale locale, String dictionaryPath, String secret, Map<String, Object> params) {
        this.locale = locale;
        this.dictionaryPath = dictionaryPath;
        this.secret = secret;
        this.params = params;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getDictionaryPath() {
        return dictionaryPath;
    }

    public String getSecret() {
        return secret;
    }

    public Object getParam(final String param) {
        return params.get(param);
    }
}
