package application.model.describer;

import application.core.anonymizer.AnonymizationStrategy;
import application.model.FieldType;

import java.io.Serializable;
import java.util.Map;

public class FieldDescriber implements Serializable, Describer {

    private static final long serialVersionUID = 1L;

    private String name;
    private FieldType type;
    private String clazz;
    private int valueLength;
    private String defaultValue;
    private boolean allowsNull;
    private boolean unique;

    private AnonymizationConfiguration configuration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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
        private AnonymizationStrategy anonymizationStrategy;
        private String secret;
        private Map<String, Object> params;

        public String getAnonymizationClass() {
            return anonymizationClass;
        }

        public void setAnonymizationClass(String anonymizationClass) {
            this.anonymizationClass = anonymizationClass;
        }

        public AnonymizationStrategy getAnonymizationStrategy() {
            return anonymizationStrategy;
        }

        public void setAnonymizationStrategy(AnonymizationStrategy anonymizationStrategy) {
            this.anonymizationStrategy = anonymizationStrategy;
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
