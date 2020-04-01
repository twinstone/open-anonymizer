package application.datasource;

import application.model.dataset.DataSet;
import application.model.describer.EntityDescriber;
import application.model.describer.RelationEntityDescriber;
import application.model.mapper.EntityMappers;
import application.model.wrapper.EntityWrapper;

import java.util.List;

public interface DataSource {

    default String getType() {
        return this.getClass().getSimpleName();
    }

    DataSet readDataSet(EntityDescriber describer);

    default DataSet readDataSet(RelationEntityDescriber describer) {
        return readDataSet(EntityMappers.getFromRelationEntity(describer));
    }

    void saveEntities(EntityDescriber describer, List<EntityWrapper> wrappers);

    void updateEntities(EntityDescriber describer, List<EntityWrapper> wrappers);

    default void saveRelationEntity(DataSource input, RelationEntityDescriber describer) {
        saveEntities(EntityMappers.getFromRelationEntity(describer), input.readDataSet(describer).all());
    }

    void saveManyToManyRelation(DataSource input, EntityDescriber leftDescriber, EntityDescriber rightDescriber);
}
