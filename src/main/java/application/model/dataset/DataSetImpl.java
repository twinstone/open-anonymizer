package application.model.dataset;

import application.model.describer.EntityDescriber;
import application.model.wrapper.EntityWrapper;

import java.util.Iterator;
import java.util.List;

public class DataSetImpl implements DataSet {

    private final List<EntityWrapper> resultSet;
    private final Iterator<EntityWrapper> iterator;
    private final EntityDescriber describer;

    public DataSetImpl(List<EntityWrapper> resultSet, EntityDescriber describer) {
        this.resultSet = resultSet;
        this.iterator = resultSet.iterator();
        this.describer = describer;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public EntityWrapper next() {
        return iterator.next();
    }

    @Override
    public long size() {
        return resultSet.size();
    }
}
