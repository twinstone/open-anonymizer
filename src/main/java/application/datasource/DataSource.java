package application.datasource;

import application.model.describer.EntityDescriber;
import application.model.wrapper.DataSet;

public abstract class DataSource {
    public abstract DataSet readDataSet(EntityDescriber describer);

    public abstract void saveDataSet(DataSet dataSet, EntityDescriber describer);
}
