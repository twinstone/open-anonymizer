package openanonymizer.model.describer;

import java.util.Map;

/**
 * This class allows to describe field structure.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class FieldDescriber implements Describer {

    private String name;
    private String defaultValue;
    private String[] defaultParams;
    private boolean allowsNull;
    private boolean unique;

    private AnonymizationConfiguration configuration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String[] getDefaultParams() {
        return defaultParams;
    }

    public void setDefaultParams(String[] defaultParams) {
        this.defaultParams = defaultParams;
    }

    public boolean allowsNull() {
        return allowsNull;
    }

    public void setAllowsNull(boolean allowsNull) {
        this.allowsNull = allowsNull;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public AnonymizationConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(AnonymizationConfiguration configuration) {
        this.configuration = configuration;
    }

    public static class AnonymizationConfiguration {
        private String anonymizationClass;
        private String secret;
        private Map<String, Object> params;

        public String getAnonymizationClass() {
            return anonymizationClass;
        }

        public void setAnonymizationClass(String anonymizationClass) {
            this.anonymizationClass = anonymizationClass;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public Map<String, Object> getParams() {
            return params;
        }

        public void setParams(Map<String, Object> params) {
            this.params = params;
        }
    }
}
