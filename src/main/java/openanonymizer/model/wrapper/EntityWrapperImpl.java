package openanonymizer.model.wrapper;

import openanonymizer.model.describer.EntityDescriber;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Default container for {@link openanonymizer.model.dataset.DataSet} values.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class EntityWrapperImpl implements EntityWrapper {

    protected final Map<String, Object> entityAsMap;
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
    public Object getId() {
        return getValue(describer.getId());
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

    @Override
    public Map<String, Object> getEntityAsMap() {
        return Collections.unmodifiableMap(entityAsMap);
    }
}
