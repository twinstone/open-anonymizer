package application.config.loader;

import application.config.Configuration;

public interface ConfigurationLoader {
    Configuration readConfiguration(String filePath);
}
