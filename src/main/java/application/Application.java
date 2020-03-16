package application;

import application.config.Configuration;
import application.config.loader.ConfigurationLoader;
import application.config.loader.JsonConfigurationLoader;
import application.core.executor.AnonymizationExecutor;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Application {

    private static final Logger logger = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        DOMConfigurator.configure("log4j.xml");
        if (args.length < 1) {
            logger.error("Invalid argument length. Stopping application.");
            return;
        }
        ConfigurationLoader loader = new JsonConfigurationLoader();
        Configuration configuration = loader.readConfiguration(args[0]);
        AnonymizationExecutor executor = new AnonymizationExecutor(configuration);
        executor.doExecute();
    }
}
