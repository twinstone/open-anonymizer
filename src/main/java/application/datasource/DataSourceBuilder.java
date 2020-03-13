package application.datasource;

public interface DataSourceBuilder<T extends DataSource, S> {
    T fromSource(final S source);
}
