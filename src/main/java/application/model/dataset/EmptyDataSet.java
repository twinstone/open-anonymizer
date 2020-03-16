package application.model.dataset;

import application.model.wrapper.EntityWrapper;

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
}
