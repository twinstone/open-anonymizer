package openanonymizer.core.stage;

import openanonymizer.config.ApplicationConfiguration;

/**
 * Interface that represents a simple stage.
 * Stages are executed in specific order, defined by configuration {@link ApplicationConfiguration}
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public interface Stage {

    /**
     * Execute current stage. Interface is not responsible for implementation.
     *
     * @param configuration application configuration class
     */
    void executeStage(final ApplicationConfiguration configuration);

}
