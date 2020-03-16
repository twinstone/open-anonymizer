package application.datasource;

import application.model.dataset.DataSet;
import application.model.describer.EntityDescriber;
import application.model.wrapper.EntityWrapper;

import java.util.List;

public interface DataSource {
    DataSet readDataSet(EntityDescriber describer);

    void saveEntity(EntityWrapper wrapper);

    void saveEntities(EntityDescriber describer, List<EntityWrapper> wrappers);

    void updateEntities(EntityDescriber describer, List<EntityWrapper> wrappers);
}
