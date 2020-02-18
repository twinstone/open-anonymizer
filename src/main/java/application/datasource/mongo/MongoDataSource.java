package application.datasource.mongo;

import application.datasource.AbstractDataSource;
import application.datasource.DataSource;
import application.model.describer.EntityDescriber;
import application.model.wrapper.DataSet;
import application.model.wrapper.MongoDataSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDataSource extends AbstractDataSource implements DataSource {

    private final MongoDatabase database;

    MongoDataSource(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public DataSet readDataSet(EntityDescriber describer) {
        MongoCollection<Document> collection = database.getCollection(describer.getSource());
        return new MongoDataSet(collection.find(), describer);
    }

    @Override
    public void saveDataSet(DataSet dataSet, EntityDescriber describer) {
        try {
            MongoCollection<Document> collection = database.getCollection(describer.getSource());
            if (collection == null) {
            }
        } catch (Exception e) {
        }
    }
}
