package application.datasource;

import application.model.describer.EntityDescriber;
import application.model.wrapper.DataSet;
import application.model.wrapper.EntityWrapper;

public interface DataSource {
    DataSet readDataSet(EntityDescriber describer);

    void saveEntity(EntityWrapper wrapper);
}
