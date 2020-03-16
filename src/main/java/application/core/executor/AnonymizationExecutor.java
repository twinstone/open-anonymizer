package application.core.executor;

import application.config.Configuration;
import application.model.describer.EntityDescriber;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnonymizationExecutor {

    private static final Logger logger = Logger.getLogger(AnonymizationExecutor.class);
    private static final ExecutorService executors = Executors.newSingleThreadExecutor();
    private final Configuration configuration;

    public AnonymizationExecutor(Configuration configuration) {
        this.configuration = configuration;
    }

    public void doExecute() {
        try {
            logger.info("Starting anonymization execution.");
            for (EntityDescriber e : configuration.getEntities()) {
                if (!executors.isTerminated()) {
                    logger.info(String.format("Submitting new Anonymization task for entity [%s].", e.getName()));
                    executors.submit(new AnonymizationTask(configuration.getInputSource(), configuration.getOutputSource(), e, configuration.getLocale(), configuration.getDictionaryPath()));
                }
                logger.info("All tasks were submitted. Waiting till tasks finished.");
                executors.shutdown();
                logger.info("All tasks finished");
            }
        } catch (Exception e) {
            logger.error("Exception during execution.", e);
            logger.info("Shutting down all tasks.");
            executors.shutdownNow();
        }
    }
}
