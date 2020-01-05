package application.core;

import application.core.anonymizer.AnonymizationStrategy;

import java.util.Map;

public class Configuration {

    private final AnonymizationStrategy strategy;
    private final Map<String, Object> params;

    public Configuration(AnonymizationStrategy strategy, Map<String, Object> params) {
        this.strategy = strategy;
        this.params = params;
    }
}
