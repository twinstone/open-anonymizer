package openanonymizer.core.stage;

import openanonymizer.config.ApplicationConfiguration;
import openanonymizer.core.executor.AnonymizationExecutor;

/**
 * This class executes anonymization process.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class AnonymizationStage implements Stage {

    @Override
    public void executeStage(ApplicationConfiguration configuration) {
        AnonymizationExecutor executor = new AnonymizationExecutor(configuration);
        executor.doExecute();
    }
}
