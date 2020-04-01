package application.datasource.mongo;

import application.datasource.DataSource;
import application.model.dataset.DataSet;
import application.model.dataset.DataSetImpl;
import application.model.dataset.EmptyDataSet;
import application.model.describer.EntityDescriber;
import application.model.describer.RelationEntityDescriber;
import application.model.mapper.EntityWrapperMapper;
import application.model.mapper.MongoEntityMapper;
import application.model.wrapper.EntityWrapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MongoDataSource implements DataSource {

    private static final EntityWrapperMapper<Document> mapper = new MongoEntityMapper();
    private static final Logger logger = Logger.getLogger(MongoDataSource.class);

    private final MongoDatabase database;

    MongoDataSource(MongoDatabase database) {
        Validate.notNull(database, "Database must be not null.");
        this.database = database;
    }

    @Override
    public DataSet readDataSet(EntityDescriber describer) {
        Validate.notNull(describer, "Describer must be not null.");
        try {
            MongoCollection<Document> collection = database.getCollection(describer.getSource());
            Validate.notNull(collection, "Collection does not exist.");
            List<EntityWrapper> wrappers = StreamSupport
                    .stream(collection.find().spliterator(), false)
                    .map(d -> mapper.getFromEntity(d, describer))
                    .collect(Collectors.toList());
            logger.info(String.format("Reading from collection [%s]. %d documents found.", describer.getSource(), wrappers.size()));
            return new DataSetImpl(wrappers, describer);
        } catch (MongoException e) {
            logger.error(String.format("Exception reading from collection [%s].", describer.getSource()), e);
            return EmptyDataSet.build();
        }
    }

    @Override
    public void saveEntities(EntityDescriber describer, List<EntityWrapper> wrappers) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notEmpty(wrappers, "Wrappers must contains at least one element.");
        MongoCollection<Document> collection = database.getCollection(describer.getSource());
        if (collection == null) collection = createEmptyCollection(describer.getSource());
        try {
            collection.insertMany(wrappers.stream().map(mapper::getFromWrapper).collect(Collectors.toList()));
            logger.info(String.format("Inserted %d documents to collection [%s].", wrappers.size(), describer.getSource()));
        } catch (MongoException e) {
            logger.error(String.format("Exception saving new entities to collection [%s].", describer.getSource()), e);
        }
    }

    @Override
    public void updateEntities(EntityDescriber describer, List<EntityWrapper> wrappers) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notEmpty(wrappers, "Wrappers must contains at least one element.");
        MongoCollection<Document> collection = database.getCollection(describer.getSource());
        try {
            for (final EntityWrapper wrapper : wrappers) {
                collection.updateOne(new BasicDBObject().append(describer.getId(), wrapper.getId()), mapper.getFromWrapper(wrapper));
                logger.info(String.format("Document with id [%s] updated.", wrapper.getId().toString()));
            }
            logger.info(String.format("Updated %d documents in collection [%s].", wrappers.size(), describer.getSource()));
        } catch (MongoException e) {
            logger.error(String.format("Exception updating entities in collection [%s].", describer.getSource()), e);
        }
    }

    @Override
    public void saveRelationEntity(DataSource input, RelationEntityDescriber describer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveManyToManyRelation(DataSource input, EntityDescriber leftDescriber, EntityDescriber rightDescriber) {
        Validate.notNull(input, "Input datasource must be not null.");
        if (input.getType().equals(this.getType())) return;
        MongoCollection<Document> collectionLeft = database.getCollection(leftDescriber.getSource());
        MongoCollection<Document> collectionRight = database.getCollection(rightDescriber.getSource());
        Validate.notNull(collectionLeft, "");
        Validate.notNull(collectionRight, "");

    }

    private MongoCollection<Document> createEmptyCollection(final String name) {
        database.createCollection(name);
        return database.getCollection(name);
    }
}
