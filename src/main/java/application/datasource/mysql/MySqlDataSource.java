package application.datasource.mysql;

import application.datasource.DataSource;
import application.datasource.SqlUtils;
import application.model.dataset.DataSet;
import application.model.dataset.DataSetImpl;
import application.model.dataset.EmptyDataSet;
import application.model.describer.EntityDescriber;
import application.model.mapper.EntityWrapperMapper;
import application.model.mapper.MySqlEntityMapper;
import application.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MySqlDataSource implements DataSource {

    private static final EntityWrapperMapper<ResultSet> mapper = new MySqlEntityMapper();

    private final Logger logger = Logger.getLogger(MySqlDataSource.class);
    private final javax.sql.DataSource dataSource;

    MySqlDataSource(javax.sql.DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSet readDataSet(EntityDescriber describer) {
        Validate.notNull(describer, "Describer must be not null.");
        try (Connection connection = dataSource.getConnection()) {
            String query = SqlUtils.sqlSelectQuery(describer);
            List<EntityWrapper> list = new LinkedList<>();
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                list.add(mapper.getFromEntity(resultSet, describer));
            }
            logger.info(String.format("Reading from dataset [%s]. %d entities found.", describer.getSource(), list.size()));
            return new DataSetImpl(list, describer);
        } catch (SQLException e) {
            logger.error(String.format("Exception reading from dataset [%s].", describer.getSource()), e);
            return EmptyDataSet.build();
        }
    }

    @Override
    public void saveEntities(EntityDescriber describer, List<EntityWrapper> wrappers) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notEmpty(wrappers, "Wrappers must contains at least one element.");
        if (!checkSourceExists(describer.getSource())) createEmptyTable(describer);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            for (final EntityWrapper wrapper : wrappers) {
                String query = SqlUtils.sqlInsertQuery(wrapper, SqlUtils.FIELD_VALUE_DELIMITER);
                int result = connection.createStatement().executeUpdate(query);
                logger.info(String.format("Saving entity to dataset [%s], %d row(s) affected.", wrapper.describeEntity().getSource(), result));
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error(String.format("Exception saving new entity to dataset [%s].", describer.getSource()), e);
        }
    }

    @Override
    public void updateEntities(EntityDescriber describer, List<EntityWrapper> wrappers) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notEmpty(wrappers, "Wrappers must contains at least one element.");
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            for (final EntityWrapper wrapper : wrappers) {
                String query = SqlUtils.sqlUpdateQuery(wrapper);
                int result = connection.createStatement().executeUpdate(query);
                logger.info(String.format("Updating entity in dataset [%s], %d row(s) affected.", wrapper.describeEntity().getSource(), result));
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error(String.format("Exception updating entity in dataset [%s].", describer.getSource()), e);
        }
    }

    @Override
    public void saveManyToManyRelation(DataSource input, EntityDescriber leftDescriber, EntityDescriber rightDescriber) {
        Validate.isTrue(checkSourceExists(leftDescriber.getSource()), "Dataset [%s] does not exists. You ");
        Validate.isTrue(checkSourceExists(rightDescriber.getSource()), "");
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
        try (Connection connection = dataSource.getConnection()) {
            String query = SqlUtils.sqlCreateTable(describer);
            connection.createStatement().execute(query);
            logger.info(String.format("Table [%s] created successfully", describer.getSource()));
        } catch (SQLException e) {
            logger.error(String.format("Exception creating new table [%s].", describer.getSource()), e);
        }
    }
}
