package application.model.wrapper;

import application.model.describer.EntityDescriber;

public interface EntityWrapper {
    EntityWrapper insert(final String field, final Object value);

    EntityWrapper update(final String field, final Object value);

    EntityDescriber describeEntity();
}
