package openanonymizer.service;

import openanonymizer.config.Configuration;
import openanonymizer.core.stage.Stage;

import java.util.Locale;

public class SimpleTestStage implements Stage {
    @Override
    public void executeStage(Configuration configuration) {
        configuration.setLocale(Locale.CHINA);
    }
}
