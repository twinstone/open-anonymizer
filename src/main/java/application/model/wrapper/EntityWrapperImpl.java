package application.model.wrapper;

import application.model.describer.EntityDescriber;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntityWrapperImpl implements EntityWrapper, Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Object> entityAsMap;
    private final EntityDescriber describer;

    public EntityWrapperImpl(final EntityDescriber describer) {
        this.entityAsMap = new LinkedHashMap<>();
        this.describer = describer;
    }

    public EntityWrapperImpl(final Map<String, Object> entityAsMap, final EntityDescriber describer) {
        this.entityAsMap = entityAsMap;
        this.describer = describer;
    }

    @Override
    public Object getValue(String field) {
        if (!entityAsMap.containsKey(field)) {
            throw new IllegalArgumentException("Could not find field with name: " + field);
        }
        return entityAsMap.get(field);
    }

    @Override
    public EntityWrapper insert(String field, Object value) {
        if (entityAsMap.containsKey(field)) {
            throw new IllegalArgumentException(String.format("Could not insert field with name [%s], already inserted.", field));
        }
        entityAsMap.put(field, value);
        return this;
    }

    @Override
    public EntityWrapper update(final String field, final Object value) {
        if (!entityAsMap.containsKey(field)) {
            throw new IllegalArgumentException(String.format("Could not update field with name [%s], not found.", field));
        }
        entityAsMap.put(field, value);
        return this;
    }

    @Override
    public EntityWrapper delete(String field) {
        entityAsMap.remove(field);
        return this;
    }

    @Override
    public EntityDescriber describeEntity() {
        return describer;
    }
}
