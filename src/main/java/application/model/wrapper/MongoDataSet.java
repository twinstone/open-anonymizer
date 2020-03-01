package application.model.wrapper;

import application.model.describer.EntityDescriber;
import application.model.mapper.EntityWrapperMapper;
import application.model.mapper.MongoEntityMapper;
import com.mongodb.client.MongoIterable;
import org.bson.Document;

import java.util.Iterator;

public class MongoDataSet implements DataSet {

    private static final EntityWrapperMapper<Document> mapper = new MongoEntityMapper();

    private final MongoIterable<Document> collection;
    private final Iterator<Document> iterator;
    private final EntityDescriber describer;

    public MongoDataSet(MongoIterable<Document> collection, final EntityDescriber describer) {
        this.collection = collection;
        this.iterator = collection.iterator();
        this.describer = describer;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public EntityWrapper next() {
        return mapper.getFromEntity(iterator.next(), describer);
    }
}
