package openanonymizer.model.mapper;

import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.describer.RelationFieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import openanonymizer.model.wrapper.EntityWrapperImpl;
import org.apache.commons.lang3.Validate;
import org.bson.Document;

/**
 * Mapper for MongoDB {@link Document}
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class MongoEntityMapper implements EntityWrapperMapper<Document> {

    @Override
    public EntityWrapper getFromEntity(Document entity, EntityDescriber describer) {
        Validate.notNull(entity, "Entity must be not null.");
        Validate.notNull(describer, "Describer must be not null.");
        EntityWrapper wrapper = new EntityWrapperImpl(describer);
        for (final FieldDescriber fieldDescriber : describer.getFields()) {
            wrapper.insert(fieldDescriber.getName(), entity.get(fieldDescriber.getName()));
        }
        if (describer.getRelationFields() != null) {
            for (final RelationFieldDescriber relationField : describer.getRelationFields()) {
                wrapper.insert(relationField.getName(), entity.get(relationField.getName()));
            }
        }
        return wrapper;
    }

    @Override
    public Document getFromWrapper(EntityWrapper wrapper) {
        return new Document(wrapper.getEntityAsMap());
    }
}
