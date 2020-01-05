package application.model.wrapper;

import application.model.describer.EntityDescriber;
import com.mongodb.client.MongoIterable;
import org.bson.Document;

import java.util.Iterator;

public class MongoDataSet implements DataSet {

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
        return new MongoEntityWrapper(iterator.next());
    }
}
