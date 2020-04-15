package openanonymizer.core.stage;

import openanonymizer.config.Configuration;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

/**
 * Class that is responsible for stage {@link Stage} execution in specific order.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class StageManager {

    private final static Logger logger = Logger.getLogger(StageManager.class);
    private final Configuration configuration;
    private final StageChain chain;

    private StageManager(final Configuration configuration, final StageChain chain) {
        this.configuration = configuration;
        this.chain = chain;
    }

    /**
     * Method that creates {@link StageManager} class.
     * Tries to create new instance of stages, defined in configuration.
     *
     * @param configuration application configuration
     * @return {@link StageManager} class
     */
    public static StageManager fromConfiguration(final Configuration configuration) {
        Validate.notNull(configuration, "Configuration must be not null.");
        Validate.notEmpty(configuration.getStages(), "Stages must be not empty.");
        StageChain.StageBuilder builder = new StageChain.StageBuilder();
        configuration.getStages().forEach(s -> {
            try {
                Class stageClazz = Class.forName(s);
                if (Stage.class.isAssignableFrom(stageClazz)) {
                    logger.info("Adding new stage of class " + s);
                    builder.withStage((Stage) stageClazz.newInstance());
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                logger.warn("Could not initialize stage of class " + s);
            }
        });
        return new StageManager(configuration, builder.build());
    }

    public void processChain() {
        chain.execute(configuration, logger);
    }

}
