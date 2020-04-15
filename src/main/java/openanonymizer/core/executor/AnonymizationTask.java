package openanonymizer.core.executor;

import openanonymizer.config.Configuration;
import openanonymizer.core.anonymizer.AnonymizationService;
import openanonymizer.datasource.PagedDataSource;
import openanonymizer.model.dataset.DataSet;
import openanonymizer.model.dataset.DataSetImpl;
import openanonymizer.model.dataset.PagedDataSet;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AnonymizationTask implements Runnable {

    private static final Logger logger = Logger.getLogger(AnonymizationTask.class);

    //Parallel running
    private final ExecutorService executors;
    private long offset = 0;
    private final ReentrantLock lock = new ReentrantLock();

    private final Configuration configuration;
    private final EntityDescriber describer;

    AnonymizationTask(final Configuration configuration,
                      final EntityDescriber describer) {
        this.configuration = configuration;
        this.describer = describer;
        executors = Executors.newFixedThreadPool(configuration.getThreads());
    }

    @Override
    public void run() {
        if (PagedDataSource.class.isAssignableFrom(configuration.getInputSource().getClass())) {
            List<Callable<Integer>> callable = IntStream.range(0, configuration.getThreads()).boxed()
                    .map(i -> processPages(lock))
                    .collect(Collectors.toList());
            invokeAll(callable);
        } else {
            List<Callable<Integer>> callable = IntStream.range(0, configuration.getThreads()).boxed()
                    .map(i -> processDataSet(configuration.getInputSource().readDataSet(describer)))
                    .collect(Collectors.toList());
            invokeAll(callable);
        }
    }

    private void invokeAll(List<Callable<Integer>> callable) {
        try {
            executors.invokeAll(callable)
                    .forEach(future -> {
                        try {
                            logger.info(String.format("Processed %s pages.", future.get()));
                        } catch (Exception e) {
                            logger.error("Exception getting future value.", e);
                        }
                    });
            executors.shutdown();
        } catch (InterruptedException e) {
            logger.error("Exception running anonymization task.", e);
        }
    }

    private Callable<Integer> processPages(final ReentrantLock lock) {
        return () -> {
            int complete = -1;
            PagedDataSet dataSet;
            do {
                lock.lock();
                long offset = this.offset;
                this.offset += this.configuration.getPageSize();
                lock.unlock();

                PagedDataSource dataSource = (PagedDataSource) configuration.getInputSource();
                dataSet = dataSource.readPage(describer, offset, configuration.getPageSize());

                while (dataSet.hasNext()) {
                    EntityWrapper wrapper = dataSet.next();
                    AnonymizationService.anonymizeEntity(wrapper, configuration.getLocale(), configuration.getDictionaryPath(), configuration.getSecret(), configuration.getLevel(), null);
                }
                if (configuration.getInputSource().equals(configuration.getOutputSource())) {
                    configuration.getInputSource().updateEntities(describer, dataSet);
                } else {
                    configuration.getOutputSource().saveEntities(describer, dataSet);
                }
                complete++;
            } while (dataSet.hasNextPage());
            return complete;
        };
    }

    private Callable<Integer> processDataSet(final DataSet dataSet) {
        return () -> {
            EntityWrapper wrapper;
            List<EntityWrapper> processed = new ArrayList<>();
            while ((wrapper = dataSet.next()) != null) {
                AnonymizationService.anonymizeEntity(wrapper, configuration.getLocale(), configuration.getDictionaryPath(), configuration.getSecret(), configuration.getLevel(), null);
                processed.add(wrapper);
            }
            if (configuration.getInputSource().equals(configuration.getOutputSource())) {
                configuration.getInputSource().updateEntities(describer, new DataSetImpl(processed, describer));
            } else {
                configuration.getOutputSource().saveEntities(describer, new DataSetImpl(processed, describer));
            }
            return processed.size();
        };
    }
}
