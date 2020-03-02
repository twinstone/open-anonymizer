package application.model.describer;

import application.core.anonymizer.AnonymizationStrategy;
import application.model.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FieldDescriber implements Serializable, Describer {

    private static final long serialVersionUID = 1L;

    private String name;
    private FieldType type;
    private String defaultValue;
    private String anonymizationClass;
    private AnonymizationStrategy anonymizationStrategy;
    private boolean allowsNull;
    private boolean unique;

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

    public Object getDefaultValue() {
        if (type == null || defaultValue == null) {

        } else if (FieldType.STRING.equals(type)) {
            return defaultValue;
        } else if (FieldType.NUMBER.equals(type)) {
            return Long.parseLong(defaultValue);
        } else if (FieldType.FLOAT.equals(type)) {
            return Double.parseDouble(defaultValue);
        } else if (FieldType.DATE.equals(type)) {
            return LocalDateTime.parse(defaultValue);
        }
        throw new IllegalArgumentException("Default value could not be set.");
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

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
}
