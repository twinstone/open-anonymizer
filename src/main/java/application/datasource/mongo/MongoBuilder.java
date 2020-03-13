package application.datasource.mongo;

import application.datasource.DataSourceBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.Validate;

public class MongoBuilder implements DataSourceBuilder<MongoDataSource, JsonNode> {

    private static final String HOST_PROPERTY = "host";
    private static final String PORT_PROPERTY = "port";
    private static final String USER_PROPERTY = "user";
    private static final String PASSWORD_PROPERTY = "password";
    private static final String DB_PROPERTY = "database";
    private static final String AUTH_DB_PROPERTY = "auth_db";

    @Override
    public MongoDataSource fromSource(JsonNode node) {
        Validate.notNull(node, "Configuration json must be a value. ");
        ConnectionString connectionString = new ConnectionString(buildUri(node));
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

    private String buildUri(JsonNode node) {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append("mongodb://");
        if (node.hasNonNull(USER_PROPERTY)) {
            uriBuilder.append(node.get(USER_PROPERTY).textValue());
            uriBuilder.append(":");
        }
        if (node.hasNonNull(PASSWORD_PROPERTY)) {
            uriBuilder.append(node.get(PASSWORD_PROPERTY).textValue());
            uriBuilder.append("@");
        }
        if (node.hasNonNull(HOST_PROPERTY)) {
            uriBuilder.append(node.get(HOST_PROPERTY).textValue());
            uriBuilder.append(":");
        }
        if (node.hasNonNull(PORT_PROPERTY)) {
            uriBuilder.append(node.get(PORT_PROPERTY).intValue());
            uriBuilder.append("/");
        }
        if (node.hasNonNull(DB_PROPERTY)) {
            uriBuilder.append(node.get(DB_PROPERTY).textValue());
        }
        return uriBuilder.toString();
    }
}
