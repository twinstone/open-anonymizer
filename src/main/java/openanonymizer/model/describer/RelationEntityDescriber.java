package openanonymizer.model.describer;

/**
 * This class allows describe relation entity structure.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class RelationEntityDescriber implements Describer {

    private String name;
    private String source;
    private RelationFieldDescriber left;
    private RelationFieldDescriber right;

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

    public RelationFieldDescriber getLeft() {
        return left;
    }

    public void setLeft(RelationFieldDescriber left) {
        this.left = left;
    }

    public RelationFieldDescriber getRight() {
        return right;
    }

    public void setRight(RelationFieldDescriber right) {
        this.right = right;
    }
}
