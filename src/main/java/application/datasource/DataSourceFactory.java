package application.datasource;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.InvocationTargetException;

public class DataSourceFactory {

    private static final String DATA_SOURCE = "data_source";

    public static DataSource getDataSourceFromJson(JsonNode node) {
        Validate.notNull(node, "Json dsnk");
        Validate.notNull(node.get(DATA_SOURCE), "Could not recognize source type. Missing configuration value.");
        try {
            //TODO refactor this
            return buildDataSourceByClazz(node.get(DATA_SOURCE).textValue(), node);
        } catch (Exception e) {
            return null;
        }
    }

    private static DataSource buildDataSourceByClazz(String clazz, JsonNode node)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> builderClass = Class.forName(clazz);
        if(builderClass.isAssignableFrom(DataSourceBuilder.class)) {
            DataSourceBuilder builder = (DataSourceBuilder) builderClass.getConstructor().newInstance();
            return builder.fromJson(node);
        }
        throw new IllegalArgumentException();
    }
}
