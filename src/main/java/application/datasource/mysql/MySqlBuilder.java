package application.datasource.mysql;

import application.datasource.DataSourceBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.mysql.cj.jdbc.MysqlDataSource;

public class MySqlBuilder implements DataSourceBuilder<MySqlDataSource, JsonNode> {

    private static final String HOST_PROPERTY = "host";
    private static final String PORT_PROPERTY = "port";
    private static final String USER_PROPERTY = "user";
    private static final String PASSWORD_PROPERTY = "password";
    private static final String DB_PROPERTY = "database";
    private static final String PARAMS_PROPERTY = "params";

    @Override
    public MySqlDataSource fromSource(JsonNode source) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(buildUri(source));
        if (source.hasNonNull(USER_PROPERTY)) {
            dataSource.setUser(source.get(USER_PROPERTY).textValue());
        }
        if (source.hasNonNull(PASSWORD_PROPERTY)) {
            dataSource.setPassword(source.get(PASSWORD_PROPERTY).textValue());
        }
        return new MySqlDataSource(dataSource);
    }

    private String buildUri(JsonNode node) {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append("jdbc:mysql://");
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
        if (node.hasNonNull(PASSWORD_PROPERTY)) {
            uriBuilder.append("?");
            uriBuilder.append(node.get(PARAMS_PROPERTY).textValue());
        }
        return uriBuilder.toString();
    }
}
