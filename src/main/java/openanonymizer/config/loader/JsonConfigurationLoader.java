package openanonymizer.config.loader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import openanonymizer.config.ApplicationConfiguration;
import openanonymizer.datasource.DataSource;
import openanonymizer.datasource.DataSourceFactory;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.RelationEntityDescriber;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

/**
 * This class allows to load application configuration from json file.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class JsonConfigurationLoader implements ConfigurationLoader {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(JsonConfigurationLoader.class);

    private static final String LOCALE = "locale";
    private static final String DICTIONARY_PATH = "dict_path";
    private static final String IN_CONNECTION = "in_connection";
    private static final String OUT_CONNECTION = "out_connection";
    private static final String ENTITIES = "entities";
    private static final String RELATION_ENTITIES = "relation_entities";
    private static final String LEVEL = "validation_level";
    private static final String STAGES = "stages";
    private static final String SECRET = "secret";
    private static final String THREADS = "threads";
    private static final String PAGE_SIZE = "page_size";

    @Override
    public ApplicationConfiguration readConfiguration(String filePath) {
        Validate.notEmpty(filePath, "Path to configuration file must be not null and not empty.");
        logger.info("Reading configuration from " + filePath);
        ApplicationConfiguration configuration = new ApplicationConfiguration();
        File config = new File(filePath);
        if (!config.exists()) {
            logger.error("Configuration file does not exist. Exiting.");
            System.exit(1);
        } else if (config.isDirectory()) {
            logger.error("Could not read configuration. It is a directory. Exiting.");
            System.exit(1);
        }
        try {
            JsonNode node = mapper.readTree(config);
            if (node.hasNonNull(IN_CONNECTION)) {
                DataSource in = DataSourceFactory.getDataSourceFromJson(node.get(IN_CONNECTION));
                configuration.setInputSource(in);
            }
            if (node.hasNonNull(OUT_CONNECTION)) {
                DataSource out = DataSourceFactory.getDataSourceFromJson(node.get(OUT_CONNECTION));
                configuration.setOutputSource(out);
            }
            if (node.hasNonNull(ENTITIES)) {
                EntityDescriber[] entities = mapper.convertValue(node.get(ENTITIES), EntityDescriber[].class);
                configuration.setEntities(Arrays.asList(entities));
            }
            if (node.hasNonNull(RELATION_ENTITIES)) {
                RelationEntityDescriber[] entities = mapper.convertValue(node.get(RELATION_ENTITIES), RelationEntityDescriber[].class);
                configuration.setRelationEntities(Arrays.asList(entities));
            }
            if(node.hasNonNull(LOCALE)) {
                configuration.setLocale(new Locale(node.get(LOCALE).textValue()));
            }
            if(node.hasNonNull(DICTIONARY_PATH)) {
                configuration.setDictionaryPath(node.get(DICTIONARY_PATH).textValue());
            }
            if (node.hasNonNull(LEVEL)) {
                configuration.setLevel(ApplicationConfiguration.ValidationLevel.valueOf(node.get(LEVEL).textValue()));
            } else {
                configuration.setLevel(ApplicationConfiguration.ValidationLevel.ERROR);
            }
            if (node.hasNonNull(SECRET)) {
                configuration.setSecret(node.get(SECRET).textValue());
            }
            if (node.hasNonNull(THREADS)) {
                configuration.setThreads(node.get(THREADS).intValue());
            }
            if (node.hasNonNull(PAGE_SIZE)) {
                configuration.setPageSize(node.get(PAGE_SIZE).intValue());
            }
            if (node.hasNonNull(STAGES)) {
                configuration.setStages(Arrays.asList(mapper.convertValue(node.get(STAGES), String[].class)));
            }
        } catch (Exception e) {
            logger.error(String.format("Exception reading configuration file [%s]. Exiting.", filePath), e);
            System.exit(1);
        }
        return configuration;
    }
}
