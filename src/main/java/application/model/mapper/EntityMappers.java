package application.model.mapper;

import application.model.describer.EntityDescriber;
import application.model.describer.FieldDescriber;
import application.model.describer.RelationEntityDescriber;
import application.model.describer.RelationFieldDescriber;

import java.util.Arrays;

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
