package application.model.mapper;

import application.model.describer.EntityDescriber;
import application.model.describer.FieldDescriber;
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
        return wrapper;
    }

    @Override
    public Document getFromWrapper(EntityWrapper wrapper) {
        Document document = new Document();
        return null;
    }
}
