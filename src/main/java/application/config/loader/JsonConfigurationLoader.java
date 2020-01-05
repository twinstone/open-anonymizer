package application.config.loader;

import application.config.Configuration;
import application.datasource.DataSource;
import application.datasource.DataSourceFactory;
import application.model.describer.EntityDescriber;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class JsonConfigurationLoader implements ConfigurationLoader {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(JsonConfigurationLoader.class);

    private static final String IN_CONNECTION = "in_connection";
    private static final String OUT_CONNECTION = "out_connection";
    private static final String ENTITIES = "entities";

    @Override
    public Configuration readConfiguration(String filePath) {
        Validate.notNull(filePath);
        logger.info("Reading configuration from " + filePath);
        Configuration configuration = new Configuration();
        File config = new File(filePath);
        if (!config.exists()) {
            logger.error("Configuration file does not exist. Exiting.");
            System.exit(1);
        } else if (config.isDirectory()) {
            logger.error("Could not read configuration. It is a directory. Existing.");
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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configuration;
    }
}
