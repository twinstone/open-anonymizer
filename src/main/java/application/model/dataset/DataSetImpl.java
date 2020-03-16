package application.model.dataset;

import application.model.describer.EntityDescriber;
import application.model.wrapper.EntityWrapper;

import java.util.Iterator;

public class DataSetImpl implements DataSet {

    private final Iterable<EntityWrapper> resultSet;
    private final Iterator<EntityWrapper> iterator;
    private final EntityDescriber describer;

    public DataSetImpl(Iterable<EntityWrapper> resultSet, EntityDescriber describer) {
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
}
