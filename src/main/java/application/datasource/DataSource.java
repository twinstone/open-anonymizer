package application.datasource;

import application.model.describer.EntityDescriber;
import application.model.wrapper.DataSet;

public interface DataSource {
    DataSet readDataSet(EntityDescriber describer);

    void saveDataSet(DataSet dataSet, EntityDescriber describer);
}
