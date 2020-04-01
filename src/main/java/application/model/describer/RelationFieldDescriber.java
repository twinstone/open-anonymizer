package application.model.describer;

import java.io.Serializable;

public class RelationFieldDescriber implements Serializable, Describer {

    private static final long serialVersionUID = 3L;

    private String name;
    private String overrideName;
    private String parentSource;
    private String targetSource;
    private RelationType relationType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverrideName() {
        return overrideName;
    }

    public void setOverrideName(String overrideName) {
        this.overrideName = overrideName;
    }

    public String getParentSource() {
        return parentSource;
    }

    public void setParentSource(String parentSource) {
        this.parentSource = parentSource;
    }

    public String getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(String targetSource) {
        this.targetSource = targetSource;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public enum RelationType {
        MANY_TO_ONE, MANY_TO_MANY
    }
}
