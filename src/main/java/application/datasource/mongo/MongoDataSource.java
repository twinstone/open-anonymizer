package application.datasource.mongo;

import application.datasource.DataSource;
import application.model.describer.EntityDescriber;
import application.model.mapper.EntityWrapperMapper;
import application.model.mapper.MongoEntityMapper;
import application.model.wrapper.DataSet;
import application.model.wrapper.EntityWrapper;
import application.model.wrapper.MongoDataSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDataSource implements DataSource {

    private static final EntityWrapperMapper<Document> mapper = new MongoEntityMapper();

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
    public void saveEntity(EntityWrapper wrapper) {
        MongoCollection<Document> collection = database.getCollection(wrapper.describeEntity().getSource());
        if (collection == null) {
            database.createCollection(wrapper.describeEntity().getSource());
            collection = database.getCollection(wrapper.describeEntity().getSource());
        }
        collection.insertOne(mapper.getFromWrapper(wrapper));
    }
}
