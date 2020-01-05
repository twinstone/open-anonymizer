package application.model.wrapper;

import org.bson.Document;

public class MongoEntityWrapper implements EntityWrapper {

    private Document document;

    public MongoEntityWrapper(Document document) {
        this.document = document;
    }

    @Override
    public void update(String field, Object value) {

    }
}
