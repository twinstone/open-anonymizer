package openanonymizer.model.describer;

/**
 * This class allows to describe relation field structure.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class RelationFieldDescriber implements Describer {

    private String name;
    private String targetSource;
    private String parentSource;
    private RelationType relationType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(String targetSource) {
        this.targetSource = targetSource;
    }

    public String getParentSource() {
        return parentSource;
    }

    public void setParentSource(String parentSource) {
        this.parentSource = parentSource;
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
