package application.datasource;

import application.datasource.mongo.MongoBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.Validate;

public class DataSourceFactory {

    private static final String DATA_SOURCE_TYPE = "data_source_type";

    public static DataSource getDataSourceFromJson(JsonNode node) {
        Validate.notNull(node, "Json dsnk");
        Validate.notNull(node.get(DATA_SOURCE_TYPE), "Could not recognize source type. Missing configuration value.");
        DataSourceType type = DataSourceType.valueOf(node.get(DATA_SOURCE_TYPE).textValue());
        return buildDataSourceByType(type, node);
    }

    private static DataSource buildDataSourceByType(DataSourceType type, JsonNode node) {
        switch (type) {
            case MONGO:
                return new MongoBuilder().fromJson(node);
            default:
                throw new IllegalArgumentException("Could not create data source for type " + type);
        }
    }
}
