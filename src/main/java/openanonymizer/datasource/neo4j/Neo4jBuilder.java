package openanonymizer.datasource.neo4j;

import com.fasterxml.jackson.databind.JsonNode;
import openanonymizer.datasource.ConnectionUriBuilder;
import openanonymizer.datasource.DataSourceBuilder;
import org.neo4j.driver.AuthToken;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;

/**
 * Builder class for {@link Neo4jDataSource}.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class Neo4jBuilder implements DataSourceBuilder<Neo4jDataSource, JsonNode> {

    private static final String USER_PROPERTY = "user";
    private static final String PASSWORD_PROPERTY = "password";

    @Override
    public Neo4jDataSource fromSource(JsonNode source) {
        AuthToken token = AuthTokens.basic(source.get(USER_PROPERTY).textValue(), source.get(PASSWORD_PROPERTY).textValue());
        return new Neo4jDataSource(GraphDatabase.driver(ConnectionUriBuilder.uriFromJson("bolt", source, false), token));
    }
}
