package openanonymizer.core.executor;

import openanonymizer.config.Configuration;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.RelationEntityDescriber;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AnonymizationExecutor {

    private static final Logger logger = Logger.getLogger(AnonymizationExecutor.class);
    private static final ExecutorService executors = Executors.newSingleThreadExecutor();
    private static final Comparator<EntityDescriber> describerComparator = Comparator.comparingInt(describer -> describer.getRelationFields() == null ? 0
            : describer.getRelationFields().size());

    private final Configuration configuration;

    public AnonymizationExecutor(Configuration configuration) {
        this.configuration = configuration;
    }

    public void doExecute() {
        try {
            logger.info("Building execution task chain.");
            configuration.getEntities().sort(describerComparator);
            logger.info("Starting anonymization execution.");
            List<Pair<String, Future<Long>>> futures = new ArrayList<>();
            for (EntityDescriber e : configuration.getEntities()) {
                if (!executors.isTerminated()) {
                    logger.info(String.format("Submitting new Anonymization task for entity [%s].", e.getName()));
                    futures.add(Pair.of(e.getName(), executors.submit(new AnonymizationTask(configuration, e))));
                }
            }
            if (configuration.getRelationEntities() != null) {
                for (RelationEntityDescriber relation : configuration.getRelationEntities()) {
                    if (!executors.isTerminated()) {
                        logger.info(String.format("Submitting new RelationUpdate task for relation entity [%s].", relation.getName()));
                        futures.add(Pair.of(relation.getName(), executors.submit(() -> {
                            long current = System.currentTimeMillis();
                            configuration.getOutputSource().saveRelationEntity(relation, configuration.getInputSource().readDataSet(relation));
                            return System.currentTimeMillis() - current;
                        })));
                    }
                }
            }
            logger.info("All tasks were submitted. Waiting till tasks finished.");
            executors.shutdown();
            for (Pair<String, Future<Long>> pair : futures) {
                logger.info(String.format("Anonymization task for entity [%s] finished in %s mills.", pair.getLeft(), pair.getRight().get()));
            }
            logger.info("All tasks finished");
        } catch (Exception e) {
            logger.error("Exception during execution.", e);
            logger.info("Shutting down all tasks.");
            executors.shutdownNow();
        }
    }
}
