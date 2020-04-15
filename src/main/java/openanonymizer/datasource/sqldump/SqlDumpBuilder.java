package openanonymizer.datasource.sqldump;

import com.fasterxml.jackson.databind.JsonNode;
import openanonymizer.datasource.DataSourceBuilder;
import org.apache.commons.lang3.Validate;

import java.io.File;

/**
 * Builder class for {@link SqlDumpDataSource}.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
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
