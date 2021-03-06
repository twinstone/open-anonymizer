package openanonymizer.model.describer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class allows to describe entity structure.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class EntityDescriber implements Describer {

    private String name;
    private String source;
    private String id;
    private List<FieldDescriber> fields;
    private List<RelationFieldDescriber> relationFields;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<FieldDescriber> getFields() {
        return fields;
    }

    public void setFields(List<FieldDescriber> fields) {
        this.fields = fields;
    }

    public List<RelationFieldDescriber> getRelationFields() {
        return relationFields;
    }

    public List<RelationFieldDescriber> getManyToOneRelationFields() {
        return relationFields == null
                ? Collections.emptyList()
                : relationFields.stream()
                .filter(f -> RelationFieldDescriber.RelationType.MANY_TO_ONE.equals(f.getRelationType()))
                .collect(Collectors.toList());
    }

    public void setRelationFields(List<RelationFieldDescriber> relationFields) {
        this.relationFields = relationFields;
    }

    public boolean containsField(String name) {
        for (FieldDescriber field : fields) {
            if (field.getName().equals(name)) {
                return true;
            }
        }
        for (RelationFieldDescriber field : relationFields) {
            if (field.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getFieldNames() {
        List<String> fields = this
                .getFields()
                .stream()
                .map(FieldDescriber::getName)
                .collect(Collectors.toList());
        fields.addAll(this
                .getManyToOneRelationFields()
                .stream()
                .map(RelationFieldDescriber::getName)
                .collect(Collectors.toList()));
        return fields;
    }
}
