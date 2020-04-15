package openanonymizer.datasource;

import openanonymizer.model.dataset.DataSet;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.RelationEntityDescriber;
import openanonymizer.model.mapper.EntityMappers;

import java.io.Closeable;

/**
 * This interface allows basic read, save and update operation on specific data source.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public interface DataSource extends Closeable {

    /**
     * Read {@link DataSet} from data source.
     *
     * @param describer entity structure
     * @return the data set
     * */
    DataSet readDataSet(EntityDescriber describer);

    /**
     * Read {@link DataSet} that represents relation from data source.
     * For example relation table in SQL database.
     *
     * @param describer relation entity structure
     * @return the data set
     */
    default DataSet readDataSet(RelationEntityDescriber describer) {
        return readDataSet(EntityMappers.getFromRelationEntity(describer));
    }

    /**
     * Saves entities.
     *
     * @param describer entity structure
     * @param dataSet   entities container
     */
    void saveEntities(EntityDescriber describer, DataSet dataSet);

    /**
     * Saves relation entities.
     *
     * @param describer relation entity structure
     * @param dataSet   entities container
     */
    default void saveRelationEntity(RelationEntityDescriber describer, DataSet dataSet) {
        saveEntities(EntityMappers.getFromRelationEntity(describer), dataSet);
    }

    /**
     * Updates entities.
     *
     * @param describer entity structure
     * @param dataSet   entities container
     */
    void updateEntities(EntityDescriber describer, DataSet dataSet);
}
