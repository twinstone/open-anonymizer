package application.datasource.sqldump;

import application.datasource.DataSourceBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.Validate;

import java.io.File;

public class SqlDumpBuilder implements DataSourceBuilder<SqlDumpDataSource, JsonNode> {

    private static final String DIR_PROPERTY = "directory";

    @Override
    public SqlDumpDataSource fromSource(JsonNode source) {
        Validate.notNull(source, "Json node must be not null.");
        Validate.notNull(source.get(DIR_PROPERTY), "Directory property must be not null.");
        File directory = new File(source.get(DIR_PROPERTY).textValue());
        return new SqlDumpDataSource(directory);
    }
}
