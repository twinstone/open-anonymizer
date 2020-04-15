package openanonymizer.model.dataset;

import openanonymizer.model.wrapper.EntityWrapper;

import java.util.Iterator;

/**
 * Container for {@link openanonymizer.datasource.DataSource} read result.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public interface DataSet extends Iterable<EntityWrapper>, Iterator<EntityWrapper> {
    /**
     * @return size of container
     * */
    long size();
}
