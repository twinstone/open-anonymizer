package openanonymizer.model.mapper;

import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.describer.RelationFieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import openanonymizer.model.wrapper.EntityWrapperImpl;
import org.apache.commons.lang3.Validate;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper for SQl {@link ResultSet}
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class MySqlEntityMapper implements EntityWrapperMapper<ResultSet> {

    @Override
    public EntityWrapper getFromEntity(ResultSet entity, EntityDescriber describer) {
        Validate.notNull(entity, "Entity must be not null.");
        Validate.notNull(describer, "Describer must be not null.");
        EntityWrapper wrapper = new EntityWrapperImpl(describer);
        try {
            for (final FieldDescriber fieldDescriber : describer.getFields()) {
                wrapper.insert(fieldDescriber.getName(), entity.getObject(fieldDescriber.getName()));
            }
            for (final RelationFieldDescriber relationField : describer.getRelationFields()) {
                wrapper.insert(relationField.getName(), entity.getObject(relationField.getName()));
            }
            return wrapper;
        } catch (SQLException e) {
            throw new MappingException(e);
        }
    }

    @Override
    public ResultSet getFromWrapper(EntityWrapper wrapper) {
        throw new UnsupportedOperationException();
    }
}
