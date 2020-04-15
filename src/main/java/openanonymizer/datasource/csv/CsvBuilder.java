package openanonymizer.datasource.csv;

import com.fasterxml.jackson.databind.JsonNode;
import openanonymizer.datasource.DataSourceBuilder;
import org.apache.commons.lang3.Validate;

import java.io.File;

/**
 * Builder class for {@link CsvDataSource}.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class CsvBuilder implements DataSourceBuilder<CsvDataSource, JsonNode> {

    private static final char DEFAULT_DELIMITER = ';';
    private static final int DEFAULT_SKIP_LINES = 0;

    private static final String DIR_PROPERTY = "directory";
    private static final String DELIMITER_PROPERTY = "delimiter";
    private static final String SKIP_LINES_PROPERTY = "skip_lines";

    @Override
    public CsvDataSource fromSource(JsonNode source) {
        Validate.notNull(source, "Json node must be not null.");
        Validate.notNull(source.get(DIR_PROPERTY), "Directory property must be not null.");
        Validate.notEmpty(source.get(DIR_PROPERTY).textValue(), "Directory property must be not empty.");
        File directory = new File(source.get(DIR_PROPERTY).textValue());
        char delimiter;
        int skipLines;
        if (source.hasNonNull(DELIMITER_PROPERTY)) {
            delimiter = source.get(DELIMITER_PROPERTY).textValue().charAt(0);
        } else {
            delimiter = DEFAULT_DELIMITER;
        }
        if (source.hasNonNull(SKIP_LINES_PROPERTY)) {
            skipLines = source.get(SKIP_LINES_PROPERTY).intValue();
        } else {
            skipLines = DEFAULT_SKIP_LINES;
        }
        return new CsvDataSource(directory, delimiter, skipLines);
    }
}
