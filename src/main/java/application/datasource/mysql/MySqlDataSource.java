package application.datasource.mysql;

import application.datasource.DataSource;
import application.model.describer.EntityDescriber;
import application.model.wrapper.DataSet;
import application.model.wrapper.EntityWrapper;

public class MySqlDataSource implements DataSource {
    @Override
    public DataSet readDataSet(EntityDescriber describer) {
        return null;
    }

    @Override
    public void saveEntity(EntityWrapper wrapper) {

    }

    @Override
    public void updateEntity(EntityWrapper wrapper) {

    }
}
