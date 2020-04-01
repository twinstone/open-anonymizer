package application.model.dataset;

import application.model.wrapper.EntityWrapper;

import java.util.List;

public interface DataSet {

    boolean hasNext();

    EntityWrapper next();

    List<EntityWrapper> all();

    long size();

    DataSet subSet(int from, int to);
}
