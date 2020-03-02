package application.model.describer;

import java.io.Serializable;
import java.util.List;

public class EntityDescriber implements Serializable, Describer {

    private static final long serialVersionUID = 2L;

    private String name;
    private String source;
    private List<FieldDescriber> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<FieldDescriber> getFields() {
        return fields;
    }

    public void setFields(List<FieldDescriber> fields) {
        this.fields = fields;
    }
}