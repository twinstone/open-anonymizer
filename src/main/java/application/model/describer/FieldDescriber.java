package application.model.describer;

import application.model.FieldDataType;
import application.model.FieldType;

import java.io.Serializable;

public class FieldDescriber implements Serializable, Describer {

    private static final long serialVersionUID = 1L;

    private String name;
    private FieldType type;
    private FieldDataType dataType;
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

    public FieldDataType getDataType() {
        return dataType;
    }

    public void setDataType(FieldDataType dataType) {
        this.dataType = dataType;
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
