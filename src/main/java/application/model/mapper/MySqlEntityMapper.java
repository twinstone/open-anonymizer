package application.model.mapper;

import application.model.describer.EntityDescriber;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;
import application.model.wrapper.EntityWrapperImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlEntityMapper implements EntityWrapperMapper<ResultSet> {

    @Override
    public EntityWrapper getFromEntity(ResultSet entity, EntityDescriber describer) {
        EntityWrapper wrapper = new EntityWrapperImpl(describer);
        try {
            for (final FieldDescriber fieldDescriber : describer.getFields()) {
                wrapper.insert(fieldDescriber.getName(), entity.getObject(fieldDescriber.getName()));
            }
        } catch (SQLException e) {
            throw new MappingException(e);
        }
        return wrapper;
    }

    @Override
    public ResultSet getFromWrapper(EntityWrapper wrapper) {
        throw new UnsupportedOperationException();
    }
}
