package openanonymizer.datasource.sql;

import openanonymizer.datasource.DataSource;
import openanonymizer.datasource.PagedDataSource;
import openanonymizer.model.dataset.*;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.mapper.EntityWrapperMapper;
import openanonymizer.model.mapper.MySqlEntityMapper;
import openanonymizer.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Data source for executing CRUD operations on MySql database.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class MySqlDataSource implements DataSource, PagedDataSource {

    private static final EntityWrapperMapper<ResultSet> mapper = new MySqlEntityMapper();
    private static final Logger logger = Logger.getLogger(MySqlDataSource.class);

    private final javax.sql.DataSource dataSource;
    private final Map<String, String> createScripts;

    public MySqlDataSource(javax.sql.DataSource dataSource, Map<String, String> createScripts) {
        Validate.notNull(dataSource, "Data source must be not null.");
        this.dataSource = dataSource;
        this.createScripts = createScripts;
    }

    @Override
    public DataSet readDataSet(EntityDescriber describer) {
        Validate.notNull(describer, "Describer must be not null.");
        String query = "";
        try (Connection connection = dataSource.getConnection()) {
            query = SqlUtils.sqlSelectQuery(describer);
            List<EntityWrapper> list = new LinkedList<>();
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                list.add(mapper.getFromEntity(resultSet, describer));
            }
            logger.info(String.format("Reading from table [%s]. %d rows found.", describer.getSource(), list.size()));
            return new DataSetImpl(list, describer);
        } catch (SQLException e) {
            logger.error(String.format("Exception reading from table [%s] using query [%s].", describer.getSource(), query), e);
            return EmptyDataSet.build();
        }
    }

    @Override
    public void saveEntities(EntityDescriber describer, DataSet dataSet) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notNull(dataSet, "Data set must be nit null.");
        String query = "";
        if (!checkSourceExists(describer.getSource())) createEmptyTable(describer);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            for (final EntityWrapper wrapper : dataSet) {
                query = SqlUtils.sqlInsertQuery(wrapper);
                int result = connection.createStatement().executeUpdate(query);
                logger.info(String.format("Saving entity to table [%s], %d row(s) affected.", wrapper.describeEntity().getSource(), result));
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error(String.format("Exception saving new entity to table [%s] using query [%s].", describer.getSource(), query), e);
        }
    }

    @Override
    public void updateEntities(EntityDescriber describer, DataSet dataSet) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notNull(dataSet, "Data set must be nit null.");
        String query = "";
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            for (final EntityWrapper wrapper : dataSet) {
                query = SqlUtils.sqlUpdateQuery(wrapper);
                int result = connection.createStatement().executeUpdate(query);
                logger.info(String.format("Updating entity in table [%s], %d row(s) affected.", wrapper.describeEntity().getSource(), result));
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error(String.format("Exception updating entity in table [%s] using query [%s].", describer.getSource(), query), e);
        }
    }

    private boolean checkSourceExists(String name) {
        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery(SqlUtils.sqlCheckTableExistsQuery(name));
            return resultSet.isFirst() && resultSet.isLast();
        } catch (SQLException e) {
            logger.error("Could not check table existence.", e);
            return false;
        }
    }

    private void createEmptyTable(EntityDescriber describer) {
        String query = createScripts.get(describer.getSource());
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute(query);
            logger.info(String.format("Table [%s] created successfully.", describer.getSource()));
        } catch (SQLException e) {
            logger.error(String.format("Exception creating new table [%s] using query [%s].", describer.getSource(), query), e);
        }
    }

    @Override
    public PagedDataSet readPage(EntityDescriber describer, long offset, int limit) {
        Validate.notNull(describer, "Describer must be not null.");
        String query = SqlUtils.sqlSelectPagedQuery(describer, offset, limit);
        try (Connection connection = dataSource.getConnection()) {
            List<EntityWrapper> list = new LinkedList<>();
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                list.add(mapper.getFromEntity(resultSet, describer));
            }
            logger.info(String.format("Reading from table [%s]. %d row(s) found.", describer.getSource(), list.size()));
            return new PagedDataSetImpl(list, describer, offset, 0, limit, getTotalItemsCount(describer));
        } catch (SQLException e) {
            logger.error(String.format("Exception reading from table [%s] using query [%S].", describer.getSource(), query), e);
            return EmptyDataSet.build();
        }
    }

    @Override
    public long getTotalItemsCount(EntityDescriber describer) {
        Validate.notNull(describer, "Describer must be not null.");
        try (Connection connection = dataSource.getConnection()) {
            String query = SqlUtils.sqlCountRowsQuery(describer);
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            logger.error(String.format("Exception reading from dataset [%s].", describer.getSource()), e);
            return 0;
        }
    }

    @Override
    public void close() {
        // do nothing
    }
}
