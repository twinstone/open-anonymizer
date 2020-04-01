package application.model.dataset;

import application.model.wrapper.EntityWrapper;

import java.util.Collections;
import java.util.List;

public class EmptyDataSet implements DataSet {

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
    public List<EntityWrapper> all() {
        return Collections.emptyList();
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public DataSet subSet(int from, int to) {
        return this;
    }
}
