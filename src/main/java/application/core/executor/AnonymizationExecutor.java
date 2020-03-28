package application.core.executor;

import application.config.Configuration;
import application.core.graph.GraphUtils;
import application.model.describer.EntityDescriber;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnonymizationExecutor {

    private static final Logger logger = Logger.getLogger(AnonymizationExecutor.class);
    private static final ExecutorService executors = Executors.newSingleThreadExecutor();
    private static final Comparator<EntityDescriber> describerComparator = Comparator.comparingInt(describer -> describer.getRelationFields().size());
    private final Configuration configuration;

    public AnonymizationExecutor(Configuration configuration) {
        this.configuration = configuration;
    }

    public void doExecute() {
        try {
            logger.info("Building execution task chain.");
            configuration.getEntities().sort(describerComparator);
            List<Pair<EntityDescriber, EntityDescriber>> mToMRelationDescribers = GraphUtils.getManyToManyRelationDescribers(configuration.getEntities());
            logger.info("Starting anonymization execution.");
            for (EntityDescriber e : configuration.getEntities()) {
                if (!executors.isTerminated()) {
                    logger.info(String.format("Submitting new Anonymization task for entity [%s].", e.getName()));
                    executors.submit(new AnonymizationTask(configuration.getInputSource(), configuration.getOutputSource(), e, configuration.getLocale(), configuration.getDictionaryPath(), null));
                }
            }
            for (Pair<EntityDescriber, EntityDescriber> relation : mToMRelationDescribers) {
                if (!executors.isTerminated()) {
                    logger.info(String.format("Submitting new RelationUpdate task for entities [%s - %s].", relation.getLeft().getName(), relation.getRight().getName()));
                    executors.submit(new RelationUpdateTask(configuration.getInputSource(), configuration.getOutputSource(), relation.getLeft(), relation.getRight()));
                }
            }
            logger.info("All tasks were submitted. Waiting till tasks finished.");
            executors.shutdown();
            logger.info("All tasks finished");
        } catch (Exception e) {
            logger.error("Exception during execution.", e);
            logger.info("Shutting down all tasks.");
            executors.shutdownNow();
        }
    }
}
