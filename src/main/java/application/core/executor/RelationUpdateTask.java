package application.core.executor;

import application.datasource.DataSource;
import application.model.describer.EntityDescriber;

public class RelationUpdateTask implements Runnable {

    private final DataSource dataSourceIn;
    private final DataSource dataSourceOut;
    private final EntityDescriber leftDescriber;
    private final EntityDescriber rightDescriber;

    public RelationUpdateTask(DataSource dataSourceIn, DataSource dataSourceOut, EntityDescriber leftDescriber, EntityDescriber rightDescriber) {
        this.dataSourceIn = dataSourceIn;
        this.dataSourceOut = dataSourceOut;
        this.leftDescriber = leftDescriber;
        this.rightDescriber = rightDescriber;
    }

    @Override
    public void run() {
        //TODO implementation
    }
}
