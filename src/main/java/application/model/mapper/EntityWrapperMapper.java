package application.model.mapper;

import application.model.describer.EntityDescriber;
import application.model.wrapper.EntityWrapper;

public interface EntityWrapperMapper<E> {
    EntityWrapper getFromEntity(final E entity, EntityDescriber describer);

    E getFromWrapper(final EntityWrapper wrapper);
}
