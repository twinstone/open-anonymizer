package application.core.executor;

import application.core.AnonymizationService;
import application.datasource.DataSource;
import application.model.dataset.DataSet;
import application.model.describer.EntityDescriber;
import application.model.wrapper.EntityWrapper;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnonymizationTask implements Runnable {

    private final DataSource dataSourceIn;
    private final DataSource dataSourceOut;
    private final EntityDescriber describer;
    private final Locale locale;
    private final String dictPath;
    private final String secret;
    //Parallel running
    private final boolean enableParallelRun = false;
    private final int entitiesPerThread = 100;
    private final ExecutorService executors;

    AnonymizationTask(final DataSource dataSourceIn, final DataSource dataSourceOut, final EntityDescriber describer, final Locale locale, final String dictPath, final String secret) {
        this.dataSourceIn = dataSourceIn;
        this.dataSourceOut = dataSourceOut;
        this.describer = describer;
        this.locale = locale;
        this.dictPath = dictPath;
        this.secret = secret;
        executors = Executors.newFixedThreadPool(5);
    }

    @Override
    public void run() {
        DataSet dataSet = dataSourceIn.readDataSet(describer);
        int pointer = 0;
        dataSet.all().stream().parallel();
        if (enableParallelRun) {
            while (pointer < dataSet.size()) {
                int from = pointer;
                int to = pointer + entitiesPerThread;
                executors.submit(() -> processDataSet(dataSet.subSet(from, to)));
                pointer += entitiesPerThread;
            }
        } else {
            processDataSet(dataSet);
        }
    }

    private void processDataSet(final DataSet dataSet) {
        while (dataSet.hasNext()) {
            EntityWrapper wrapper = dataSet.next();
            AnonymizationService.anonymizeEntity(wrapper, locale, dictPath, secret);
        }
        if (dataSourceIn.equals(dataSourceOut)) {
            dataSourceIn.updateEntities(describer, dataSet.all());
        } else {
            dataSourceOut.saveEntities(describer, dataSet.all());
        }
    }
}
