package openanonymizer.datasource;

/**
 * Basic application {@link DataSource} builder. Could be applied to any type of source.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public interface DataSourceBuilder<T extends DataSource, S> {

    /**
     * This method creates instance on data source T from source S.
     *
     * @param source any source
     * @return instance of T that implements {@link DataSource}
     */
    T fromSource(final S source);
}
