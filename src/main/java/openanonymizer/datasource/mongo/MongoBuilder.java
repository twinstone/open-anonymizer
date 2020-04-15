package openanonymizer.datasource.mongo;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import openanonymizer.datasource.ConnectionUriBuilder;
import openanonymizer.datasource.DataSourceBuilder;
import org.apache.commons.lang3.Validate;

/**
 * Builder class for {@link MongoDataSource}.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class MongoBuilder implements DataSourceBuilder<MongoDataSource, JsonNode> {

    private static final String DB_PROPERTY = "database";

    @Override
    public MongoDataSource fromSource(JsonNode node) {
        Validate.notNull(node, "Configuration json must be a value. ");
        ConnectionString connectionString = new ConnectionString(ConnectionUriBuilder.uriFromJson("mongodb", node, true));
        MongoClientSettings settings = MongoClientSettings
                .builder()
                .applyConnectionString(connectionString)
                .retryReads(true)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase(node.get(DB_PROPERTY).textValue());
        return new MongoDataSource(database);
    }
}
