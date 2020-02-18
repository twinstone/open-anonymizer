package application;

import application.config.Configuration;
import application.config.loader.ConfigurationLoader;
import application.config.loader.JsonConfigurationLoader;
import application.core.AnonymizationService;
import application.datasource.DataSource;
import application.model.wrapper.DataSet;
import application.model.wrapper.EntityWrapper;
import org.junit.Test;

public class ConfigurationLoaderTest {

    private final static String PATH = "F:/Bachelor/src/main/resources/config.json";

    @Test
    public void test() {
        ConfigurationLoader loader = new JsonConfigurationLoader();
        Configuration configuration = loader.readConfiguration(PATH);
        AnonymizationService service = new AnonymizationService();
        DataSource dataSource = configuration.getInputSource();
        DataSet dataSet = dataSource.readDataSet(configuration.getEntities().get(0));
        while (dataSet.hasNext()) {
            EntityWrapper wrapper = dataSet.next();
            service.anonymizeEntity(wrapper, configuration.getEntities().get(0), configuration);
        }
    }

}
