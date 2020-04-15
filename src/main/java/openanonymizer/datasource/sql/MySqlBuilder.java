package openanonymizer.datasource.sql;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.mysql.cj.jdbc.MysqlDataSource;
import openanonymizer.datasource.ConnectionUriBuilder;
import openanonymizer.datasource.DataSourceBuilder;

import java.util.Iterator;
import java.util.Map;

/**
 * Builder class for {@link MysqlDataSource}.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class MySqlBuilder implements DataSourceBuilder<MySqlDataSource, JsonNode> {

    private static final String CREATE_SCRIPTS_PROPERTY = "create_scripts";

    @Override
    public MySqlDataSource fromSource(JsonNode source) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(ConnectionUriBuilder.uriFromJson("jdbc:mysql", source, true));
        Map<String, String> scripts = Maps.newHashMap();
        if (source.hasNonNull(CREATE_SCRIPTS_PROPERTY)) {
            Iterator<Map.Entry<String, JsonNode>> iterator = source.get(CREATE_SCRIPTS_PROPERTY).fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();
                scripts.put(entry.getKey(), entry.getValue().textValue());
            }
        }
        return new MySqlDataSource(dataSource, scripts);
    }
}
