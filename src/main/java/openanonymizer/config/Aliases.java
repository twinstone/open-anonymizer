package openanonymizer.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains aliases for {@link openanonymizer.datasource.DataSourceBuilder} and {@link openanonymizer.anonymizer.Anonymizer}.
 * You could use specific key instead of using whole class path.
 * <p>
 * Use static methods of this class. Do not use constructor.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class Aliases {

    /**
     * Alias of anonymizers that implement {@link openanonymizer.anonymizer.Anonymizer} interface.
     */
    private final static Map<String, String> ANONYMIZERS_ALIAS = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("STRING_DEFAULT", "openanonymizer.anonymizer.DefaultStringAnonymizer");
        put("INTEGER_DEFAULT", "openanonymizer.anonymizer.DefaultIntegerAnonymizer");
        put("DELETE", "openanonymizer.anonymizer.DeleteAnonymizer");
        put("HASH", "openanonymizer.anonymizer.HashAnonymizer");
        put("DICTIONARY", "openanonymizer.anonymizer.DictionaryBasedAnonymizer");
        put("PATTERN", "openanonymizer.anonymizer.PatternBasedAnonymizer");
        put("MULTI", "openanonymizer.anonymizer.MultiDictionaryAndPatternBasedAnonymizer");
    }});

    /**
     * Alias of builders that implement {@link openanonymizer.datasource.DataSourceBuilder} interface.
     */
    private final static Map<String, String> BUILDERS_ALIAS = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("MONGO", "openanonymizer.datasource.mongo.MongoBuilder");
        put("MYSQL", "openanonymizer.datasource.sql.MySqlBuilder");
        put("CSV", "openanonymizer.datasource.csv.CsvBuilder");
        put("SQL_FILE", "openanonymizer.datasource.sqldump.SqlDumpBuilder");
        put("NEO4J", "openanonymizer.datasource.neo4j.Neo4jBuilder");
    }});

    /**
     * Returns builder class path
     *
     * @param name key in BUILDERS_ALIAS
     * @return alias value if found or input name
     */
    public static String tryFindBuilderClass(final String name) {
        if (BUILDERS_ALIAS.containsKey(name)) {
            return BUILDERS_ALIAS.get(name);
        }
        return name;
    }

    /**
     * Returns anonymizer class path
     *
     * @param name key in ANONYMIZERS_ALIAS
     * @return alias value if found or input name
     */
    public static String tryFindAnonymizationClass(final String name) {
        if (ANONYMIZERS_ALIAS.containsKey(name)) {
            return ANONYMIZERS_ALIAS.get(name);
        }
        return name;
    }

    private Aliases() {
        throw new IllegalStateException();
    }
}
