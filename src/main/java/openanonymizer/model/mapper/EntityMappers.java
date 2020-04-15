package openanonymizer.model.mapper;

import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.describer.RelationEntityDescriber;
import openanonymizer.model.describer.RelationFieldDescriber;

import java.util.Arrays;

/**
 * This class allows conversation from {@link RelationEntityDescriber} ro {@link EntityDescriber}
 * and from {@link RelationFieldDescriber} to {@link FieldDescriber}
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class EntityMappers {

    public static EntityDescriber getFromRelationEntity(RelationEntityDescriber relationDescriber) {
        EntityDescriber describer = new EntityDescriber();
        describer.setName(relationDescriber.getName());
        describer.setSource(relationDescriber.getSource());
        FieldDescriber left = getFromRelationField(relationDescriber.getLeft());
        FieldDescriber right = getFromRelationField(relationDescriber.getRight());
        describer.setFields(Arrays.asList(left, right));
        return describer;
    }

    public static FieldDescriber getFromRelationField(RelationFieldDescriber fieldDescriber) {
        FieldDescriber describer = new FieldDescriber();
        describer.setName(fieldDescriber.getName());
        return describer;
    }

    private EntityMappers() {
        throw new IllegalStateException();
    }
}
