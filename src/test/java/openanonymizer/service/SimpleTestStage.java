package openanonymizer.service;

import openanonymizer.config.ApplicationConfiguration;
import openanonymizer.core.stage.Stage;

import java.util.Locale;

/**
 * Simple test stage class. No functionality expected.
 */
public class SimpleTestStage implements Stage {
    @Override
    public void executeStage(ApplicationConfiguration configuration) {
        configuration.setLocale(Locale.CHINA);
    }
}
