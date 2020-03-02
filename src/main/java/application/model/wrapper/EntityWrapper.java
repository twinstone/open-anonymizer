package application.model.wrapper;

import application.model.describer.EntityDescriber;

import java.util.Map;

public interface EntityWrapper {

    Object getValue(final String field);

    EntityWrapper insert(final String field, final Object value);

    EntityWrapper update(final String field, final Object value);

    EntityWrapper delete(final String field);

    EntityDescriber describeEntity();

    Map<String, Object> getEntityAsMap();
}
