package openanonymizer.model.wrapper;

import openanonymizer.model.describer.EntityDescriber;

import java.util.Map;

/**
 * Container for {@link openanonymizer.model.dataset.DataSet} values.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public interface EntityWrapper {

    /**
     * @return id of entity
     */
    Object getId();

    /**
     * @param field name
     * @return field value by field key
     */
    Object getValue(final String field);

    /**
     * Inserts new field with its value into container
     *
     * @param field name
     * @param value inserted value
     * @return entity container
     * */
    EntityWrapper insert(final String field, final Object value);

    /**
     * Updates existing field value
     *
     * @param field name
     * @param value updated value
     * @return entity container
     * */
    EntityWrapper update(final String field, final Object value);

    /**
     * Removes field from container
     *
     * @param field name
     * @return entity container
     * */
    EntityWrapper delete(final String field);

    /**
     * @return wrapped entity structure
     * */
    EntityDescriber describeEntity();

    /**
     * @return entity as {@link Map}
     * */
    Map<String, Object> getEntityAsMap();
}
