package openanonymizer.datasource;

import com.fasterxml.jackson.databind.JsonNode;
import openanonymizer.config.Aliases;
import org.apache.commons.lang3.Validate;

public class DataSourceFactory {

    private static final String DATA_SOURCE = "data_source_builder";

    @SuppressWarnings("unchecked")
    public static DataSource getDataSourceFromJson(final JsonNode node) {
        Validate.notNull(node, "Json node must be not null.");
        Validate.notNull(node.get(DATA_SOURCE), "Could not recognize source type. Missing configuration value.");
        try {
            Class<?> builderClass = Class.forName(Aliases.tryFindBuilderClass(node.get(DATA_SOURCE).textValue()));
            if (DataSourceBuilder.class.isAssignableFrom(builderClass)) {
                DataSourceBuilder builder = (DataSourceBuilder) builderClass.getConstructor().newInstance();
                return builder.fromSource(node);
            }
            throw new IllegalArgumentException("Could not build DataSource. " + builderClass + " does not implements " + DataSourceBuilder.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
