package application.datasource.mysql;

import application.datasource.DataSourceBuilder;
import com.fasterxml.jackson.databind.JsonNode;

public class MySqlBuilder implements DataSourceBuilder<MySqlDataSource, JsonNode> {

    private static final String HOST_PROPERTY = "host";
    private static final String PORT_PROPERTY = "port";
    private static final String USER_PROPERTY = "user";
    private static final String PASSWORD_PROPERTY = "password";
    private static final String DB_PROPERTY = "database";

    @Override
    public MySqlDataSource fromSource(JsonNode source) {
        return null;
    }
}
