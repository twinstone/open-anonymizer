package application.core.executor;

import application.core.AnonymizationService;
import application.datasource.DataSource;
import application.model.dataset.DataSet;
import application.model.describer.EntityDescriber;
import application.model.wrapper.EntityWrapper;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class AnonymizationTask implements Runnable {

    private final static Logger logger = Logger.getLogger(AnonymizationTask.class);

    private final DataSource dataSourceIn;
    private final DataSource dataSourceOut;
    private final EntityDescriber describer;
    private final Locale locale;
    private final String dictPath;
    private final String secret;

    AnonymizationTask(final DataSource dataSourceIn, final DataSource dataSourceOut, final EntityDescriber describer, final Locale locale, final String dictPath, final String secret) {
        this.dataSourceIn = dataSourceIn;
        this.dataSourceOut = dataSourceOut;
        this.describer = describer;
        this.locale = locale;
        this.dictPath = dictPath;
        this.secret = secret;
    }

    @Override
    public void run() {
        logger.info("Reading new data set from ...");
        DataSet dataSet = dataSourceIn.readDataSet(describer);
        List<EntityWrapper> wrappers = new LinkedList<>();
        while (dataSet.hasNext()) {
            EntityWrapper wrapper = dataSet.next();
            AnonymizationService.anonymizeEntity(wrapper, locale, dictPath, secret);
            wrappers.add(wrapper);
        }
        if (dataSourceIn.equals(dataSourceOut)) {
            dataSourceIn.updateEntities(describer, wrappers);
        } else {
            dataSourceOut.saveEntities(describer, wrappers);
        }
    }
}
