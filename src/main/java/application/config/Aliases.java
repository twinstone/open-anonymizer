package application.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Aliases {

    private final static Map<String, String> ANONYMIZERS_ALIAS = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("EMAIL", "application.core.anonymizer.EmailAnonymizer");
        put("FIRST_NAME", "application.core.anonymizer.FirstNameAnonymizer");
    }});

    private final static Map<String, String> BUILDERS_ALIAS = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("MONGO", "application.datasource.mongo.MongoBuilder");
        put("MYSQL", "application.datasource.mysql.MySqlBuilder");
        put("CSV", "application.datasource.csv.CsvBuilder");
    }});

    public static String tryFind(final String key) {
        return null;
    }

    public static String tryFindBuilderClass(String name) {
        if (BUILDERS_ALIAS.containsKey(name)) {
            return BUILDERS_ALIAS.get(name);
        }
        return name;
    }

    public static String tryFindAnonymizationClass(String name) {
        if (ANONYMIZERS_ALIAS.containsKey(name)) {
            return ANONYMIZERS_ALIAS.get(name);
        }
        return name;
    }
}
