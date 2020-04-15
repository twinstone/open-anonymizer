package openanonymizer.model.dataset;

/**
 * Container for {@link openanonymizer.datasource.PagedDataSource} read result.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public interface PagedDataSet extends DataSet {

    long getOffset();

    int getPageNumber();

    int getPageSize();

    long getTotalItems();

    boolean hasNextPage();
}
