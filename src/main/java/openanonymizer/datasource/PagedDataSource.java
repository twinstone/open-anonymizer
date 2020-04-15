package openanonymizer.datasource;

import openanonymizer.model.dataset.PagedDataSet;
import openanonymizer.model.describer.EntityDescriber;

/**
 * This interface allows read operation on specific data source with offset and limit restrictions.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public interface PagedDataSource {

    /**
     * @param describer entity structure
     * @param offset    entity to start from
     * @param limit     max entities to be returned
     * @return paged data set
     */
    PagedDataSet readPage(EntityDescriber describer, long offset, int limit);

    /**
     * @param describer entity structure
     * @return total items count in specific data set, etc.
     */
    long getTotalItemsCount(EntityDescriber describer);

}
