package openanonymizer.core.stage;

import openanonymizer.config.Configuration;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is a simple stages container.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
class StageChain {

    private final List<Stage> stages;

    private StageChain(List<Stage> stages) {
        this.stages = stages;
    }

    /**
     * Executes each stage and log output
     *
     * @param configuration application configuration
     * @param logger
     */
    void execute(final Configuration configuration, final Logger logger) {
        for (final Stage stage : stages) {
            logger.info(String.format("Executing stage [%s].", stage.getClass().getSimpleName()));
            stage.executeStage(configuration);
            logger.info("Stage completed.");
        }
        logger.info("All stages completed.");
    }

    /**
     * Builder class for {@link StageChain}
     */
    public static class StageBuilder {

        private List<Stage> stages = new LinkedList<>();

        StageBuilder withStage(final Stage stage) {
            stages.add(stage);
            return this;
        }

        public StageChain build() {
            return new StageChain(stages);
        }
    }

}
