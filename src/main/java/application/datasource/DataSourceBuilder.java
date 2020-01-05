package application.datasource;

import com.fasterxml.jackson.databind.JsonNode;

public interface DataSourceBuilder<T extends DataSource> {
    T fromJson(JsonNode node);
}
