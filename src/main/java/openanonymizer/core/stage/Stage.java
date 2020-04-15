package openanonymizer.core.stage;

import openanonymizer.config.Configuration;

/**
 * Interface that represents a simple stage.
 * Stages are executed in specific order, defined by configuration {@link Configuration}
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
    void executeStage(final Configuration configuration);

}
