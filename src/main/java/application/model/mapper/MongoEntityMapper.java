package application.model.mapper;

import application.model.describer.EntityDescriber;
import application.model.describer.FieldDescriber;
import application.model.describer.RelationFieldDescriber;
import application.model.wrapper.EntityWrapper;
import application.model.wrapper.EntityWrapperImpl;
import org.bson.Document;

public class MongoEntityMapper implements EntityWrapperMapper<Document> {
    @Override
    public EntityWrapper getFromEntity(Document entity, EntityDescriber describer) {
        EntityWrapper wrapper = new EntityWrapperImpl(describer);
        for (final FieldDescriber fieldDescriber : describer.getFields()) {
            wrapper.insert(fieldDescriber.getName(), entity.get(fieldDescriber.getName()));
        }
        for (final RelationFieldDescriber relationField : describer.getRelationFields()) {
            wrapper.insert(relationField.getName(), entity.get(relationField.getName()));
        }
        return wrapper;
    }

    @Override
    public Document getFromWrapper(EntityWrapper wrapper) {
        return new Document(wrapper.getEntityAsMap());
    }
}
