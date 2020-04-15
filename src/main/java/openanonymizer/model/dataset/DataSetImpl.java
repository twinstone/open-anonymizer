package openanonymizer.model.dataset;

import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.wrapper.EntityWrapper;

import java.util.Iterator;
import java.util.List;

/**
 * Container for result returned by read operation from {@link openanonymizer.datasource.DataSource}
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class DataSetImpl implements DataSet {

    private final List<EntityWrapper> resultSet;
    private final Iterator<EntityWrapper> iterator;
    private final EntityDescriber describer;

    public DataSetImpl(List<EntityWrapper> resultSet, EntityDescriber describer) {
        this.resultSet = resultSet;
        this.iterator = resultSet.iterator();
        this.describer = describer;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public synchronized EntityWrapper next() {
        if (!iterator.hasNext()) return null;
        return iterator.next();
    }

    @Override
    public Iterator<EntityWrapper> iterator() {
        return iterator;
    }

    @Override
    public long size() {
        return resultSet.size();
    }
}
