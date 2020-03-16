package application.model.mapper;

import application.model.describer.EntityDescriber;
import application.model.wrapper.EntityWrapper;
import application.model.wrapper.EntityWrapperImpl;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvEntityMapper implements EntityWrapperMapper<String[]> {

    private final Map<String, Integer> indexMapping;

    public CsvEntityMapper(List<String> fields, EntityDescriber describer) {
        Validate.notEmpty(fields, "Fields must be not null or empty.");
        Validate.notNull(describer, "Describer must be not null.");
        indexMapping = new HashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            if (describer.containsField(field)) {
                indexMapping.put(field, i);
            }
        }
    }

    @Override
    public EntityWrapper getFromEntity(String[] entity, EntityDescriber describer) {
        Validate.notEmpty(entity, "Entity must be not empty array.");
        Validate.notNull(describer, "Describer must be not null.");
        EntityWrapper wrapper = new EntityWrapperImpl(describer);
        try {
            indexMapping.forEach((k, v) -> wrapper.insert(k, entity[v]));
        } catch (IndexOutOfBoundsException e) {
            throw new MappingException(e);
        }
        return wrapper;
    }

    @Override
    public String[] getFromWrapper(EntityWrapper wrapper) {
        Validate.notNull(wrapper, "Wrapper must be not null.");
        try {
            return wrapper.getEntityAsMap()
                    .values()
                    .stream()
                    .map(Object::toString)
                    .toArray(String[]::new);
        } catch (Exception e) {
            throw new MappingException(e);
        }
    }
}
