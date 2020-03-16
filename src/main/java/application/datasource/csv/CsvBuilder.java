package application.datasource.csv;

import application.datasource.DataSourceBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CsvBuilder implements DataSourceBuilder<CsvDataSource, JsonNode> {

    private static final char DEFAULT_DELIMITER = ';';
    private static final int DEFAULT_SKIP_LINES = 0;

    private static final String DIR_PROPERTY = "directory";
    private static final String DELIMITER_PROPERTY = "delimiter";
    private static final String SKIP_LINES_PROPERTY = "skip_lines";
    private static final String FIELDS_PROPERTY = "fields";

    @Override
    public CsvDataSource fromSource(JsonNode source) {
        Validate.notNull(source, "Json node must be not null.");
        Validate.notNull(source.get(DIR_PROPERTY), "Directory property must be not null.");
        Validate.notNull(source.get(FIELDS_PROPERTY), "Fields property must be not null.");
        Validate.notEmpty(source.get(DIR_PROPERTY).textValue(), "Directory property must be not empty.");
        Validate.isTrue(source.get(FIELDS_PROPERTY).isArray(), "Fields property must be an elements array.");
        Validate.isTrue(source.get(FIELDS_PROPERTY).elements().hasNext(), "Fields property must contains at least one element.");
        File directory = new File(source.get(DIR_PROPERTY).textValue());
        List<String> fields = new ArrayList<>();
        source.get(FIELDS_PROPERTY).elements().forEachRemaining(e -> fields.add(e.textValue()));
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
        return new CsvDataSource(directory, delimiter, skipLines, fields);
    }
}
