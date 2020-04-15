package openanonymizer.model.dataset;

import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.wrapper.EntityWrapper;

import java.util.List;

/**
 * Container for result returned by read operation from {@link openanonymizer.datasource.PagedDataSource}
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class PagedDataSetImpl extends DataSetImpl implements PagedDataSet {

    private final long offset;
    private final int pageNumber;
    private final int pageSize;
    private final long totalItems;

    public PagedDataSetImpl(List<EntityWrapper> resultSet, EntityDescriber describer, long offset, int pageNumber, int pageSize, long totalItems) {
        super(resultSet, describer);
        this.offset = offset;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long getTotalItems() {
        return totalItems;
    }

    @Override
    public boolean hasNextPage() {
        return offset + pageSize < totalItems;
    }
}
