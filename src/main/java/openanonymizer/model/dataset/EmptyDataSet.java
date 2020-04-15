package openanonymizer.model.dataset;

import openanonymizer.model.wrapper.EntityWrapper;

import java.util.Iterator;

/**
 * Container for empty data set.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class EmptyDataSet implements PagedDataSet {

    public static EmptyDataSet build() {
        return new EmptyDataSet();
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public EntityWrapper next() {
        return null;
    }

    @Override
    public long getOffset() {
        return 0;
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return 0;
    }

    @Override
    public long getTotalItems() {
        return 0;
    }

    @Override
    public boolean hasNextPage() {
        return false;
    }

    @Override
    public Iterator<EntityWrapper> iterator() {
        return null;
    }

    @Override
    public long size() {
        return 0;
    }
}
