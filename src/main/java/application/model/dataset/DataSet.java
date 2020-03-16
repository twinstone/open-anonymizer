package application.model.dataset;

import application.model.wrapper.EntityWrapper;

public interface DataSet {

    boolean hasNext();

    EntityWrapper next();
}
