package application.config.loader;

import application.config.Configuration;
import application.datasource.DataSource;
import application.datasource.DataSourceFactory;
import application.model.describer.EntityDescriber;
import application.model.describer.RelationEntityDescriber;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

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

    @Override
    public Configuration readConfiguration(String filePath) {
        Validate.notEmpty(filePath, "Path to configuration file must be not null and not empty.");
        logger.info("Reading configuration from " + filePath);
        Configuration configuration = new Configuration();
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
                configuration.setLevel(Configuration.ValidationLevel.valueOf(node.get(LEVEL).textValue()));
            } else {
                configuration.setLevel(Configuration.ValidationLevel.ERROR);
            }
        } catch (Exception e) {
            logger.error(String.format("Exception reading configuration file [%s]. Exiting.", filePath), e);
            System.exit(1);
        }
        return configuration;
    }
}
