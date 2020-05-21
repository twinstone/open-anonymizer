package openanonymizer.datasource.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import openanonymizer.datasource.DataSource;
import openanonymizer.datasource.PagedDataSource;
import openanonymizer.model.dataset.*;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.RelationEntityDescriber;
import openanonymizer.model.mapper.EntityWrapperMapper;
import openanonymizer.model.mapper.MongoEntityMapper;
import openanonymizer.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Data source for executing CRUD operations on Mongo database.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class MongoDataSource implements DataSource, PagedDataSource {

    private static final EntityWrapperMapper<Document> mapper = new MongoEntityMapper();
    private static final Logger logger = Logger.getLogger(MongoDataSource.class);

    private final MongoDatabase database;

    public MongoDataSource(MongoDatabase database) {
        Validate.notNull(database, "Database must be not null.");
        this.database = database;
    }

    @Override
    public DataSet readDataSet(EntityDescriber describer) {
        Validate.notNull(describer, "Describer must be not null.");
        try {
            MongoCollection<Document> collection = database.getCollection(describer.getSource(), Document.class);
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
    public void saveEntities(EntityDescriber describer, DataSet dataSet) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notNull(dataSet, "Data set must be nit null.");
        MongoCollection<Document> collection = database.getCollection(describer.getSource(), Document.class);
        try {
            List<Document> documents = StreamSupport.stream(dataSet.spliterator(), false).map(mapper::getFromWrapper).collect(Collectors.toList());
            if (!documents.isEmpty()) {
                collection.insertMany(documents);
            }
            logger.info(String.format("Documents inserted into collection [%s].", describer.getSource()));
        } catch (MongoException e) {
            logger.error(String.format("Exception saving new entities to collection [%s].", describer.getSource()), e);
        }
    }

    @Override
    public void updateEntities(EntityDescriber describer, DataSet dataSet) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notNull(dataSet, "Data set must be nit null.");
        MongoCollection<Document> collection = database.getCollection(describer.getSource(), Document.class);
        try {
            long counter = 0;
            for (final EntityWrapper wrapper : dataSet) {
                collection.updateOne(new BasicDBObject().append(describer.getId(), wrapper.getId()), mapper.getFromWrapper(wrapper));
                logger.info(String.format("Document with id [%s] updated.", wrapper.getId().toString()));
                counter++;
            }
            logger.info(String.format("Updated %d documents in collection [%s].", counter, describer.getSource()));
        } catch (MongoException e) {
            logger.error(String.format("Exception updating entities in collection [%s].", describer.getSource()), e);
        }
    }

    @Override
    public void saveRelationEntity(RelationEntityDescriber describer, DataSet dataSet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PagedDataSet readPage(EntityDescriber describer, long offset, int limit) {
        Validate.notNull(describer, "Describer must be not null.");
        try {
            MongoCollection<Document> collection = database.getCollection(describer.getSource(), Document.class);
            Validate.notNull(collection, "Collection does not exist.");
            List<EntityWrapper> wrappers = StreamSupport
                    .stream(collection.find().spliterator(), false)
                    .skip(offset)
                    .limit(limit)
                    .map(d -> mapper.getFromEntity(d, describer))
                    .collect(Collectors.toList());
            logger.info(String.format("Reading from collection [%s]. %d documents found.", describer.getSource(), wrappers.size()));
            return new PagedDataSetImpl(wrappers, describer, offset, 0, limit, getTotalItemsCount(describer));
        } catch (MongoException e) {
            logger.error(String.format("Exception reading from collection [%s].", describer.getSource()), e);
            return EmptyDataSet.build();
        }
    }

    @Override
    public long getTotalItemsCount(EntityDescriber describer) {
        Validate.notNull(describer, "Describer must be not null.");
        try {
            MongoCollection<Document> collection = database.getCollection(describer.getSource(), Document.class);
            Validate.notNull(collection, "Collection does not exist.");
            return collection.countDocuments();
        } catch (MongoException e) {
            logger.error(String.format("Exception reading from collection [%s].", describer.getSource()), e);
            return 0;
        }
    }

    @Override
    public void close() {
        //do nothing
    }
}
