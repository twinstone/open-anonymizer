package openanonymizer.model.mapper;

import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.wrapper.EntityWrapper;

/**
 * This interface allows conversation between entity E and {@link EntityWrapper}
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 **/
public interface EntityWrapperMapper<E> {

    /**
     * @param entity    any entity
     * @param describer entity structure
     * @return {@link EntityWrapper} converted from entity E.
     */
    EntityWrapper getFromEntity(final E entity, EntityDescriber describer);

    /**
     * @param wrapper entity container
     * @return converted entity E from {@link EntityWrapper}.
     * */
    E getFromWrapper(final EntityWrapper wrapper);
}
