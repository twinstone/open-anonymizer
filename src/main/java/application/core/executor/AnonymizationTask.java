package application.core.executor;

import application.core.AnonymizationService;
import application.datasource.DataSource;
import application.model.describer.EntityDescriber;
import application.model.wrapper.DataSet;
import application.model.wrapper.EntityWrapper;
import org.apache.log4j.Logger;

import java.util.Locale;

public class AnonymizationTask implements Runnable {

    private final static Logger logger = Logger.getLogger(AnonymizationTask.class);

    private final DataSource dataSourceIn;
    private final DataSource dataSourceOut;
    private final EntityDescriber describer;
    private final Locale locale;
    private final String dictPath;

    public AnonymizationTask(final DataSource dataSourceIn, final DataSource dataSourceOut, final EntityDescriber describer, final Locale locale, final String dictPath) {
        this.dataSourceIn = dataSourceIn;
        this.dataSourceOut = dataSourceOut;
        this.describer = describer;
        this.locale = locale;
        this.dictPath = dictPath;
    }

    @Override
    public void run() {
        logger.info("Reading new data set from ...");
        DataSet dataSet = dataSourceIn.readDataSet(describer);
        while (dataSet.hasNext()) {
            EntityWrapper wrapper = dataSet.next();
            AnonymizationService.anonymizeEntity(wrapper, locale, dictPath);
            if (dataSourceIn.equals(dataSourceOut)) {
                dataSourceIn.updateEntity(wrapper);
            } else {
                dataSourceOut.saveEntity(wrapper);
            }
        }
    }
}
