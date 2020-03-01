package application.model.describer;

import application.core.anonymizer.AnonymizationStrategy;
import application.model.FieldType;

import java.io.Serializable;

public class FieldDescriber implements Serializable, Describer {

    private static final long serialVersionUID = 1L;

    private String name;
    private FieldType type;
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

    public boolean isAllowsNull() {
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
