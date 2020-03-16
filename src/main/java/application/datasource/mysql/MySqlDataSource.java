package application.datasource.mysql;

import application.datasource.DataSource;
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
import java.util.StringJoiner;

public class MySqlDataSource implements DataSource {

    private static final EntityWrapperMapper<ResultSet> mapper = new MySqlEntityMapper();
    private static final String FIELD_VALUE_DELIMITER = ", ";

    private final Logger logger = Logger.getLogger(MySqlDataSource.class);
    private final javax.sql.DataSource dataSource;

    MySqlDataSource(javax.sql.DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSet readDataSet(EntityDescriber describer) {
        Validate.notNull(describer, "Describer must be not null.");
        try (Connection connection = dataSource.getConnection()) {
            String query = sqlSelectQuery(describer);
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
    public void saveEntity(EntityWrapper wrapper) {
        if (!checkSourceExists(wrapper.describeEntity().getSource())) {

        }
        try (Connection connection = dataSource.getConnection()) {
            String query = sqlInsertQuery(wrapper);
            int result = connection.createStatement().executeUpdate(query);
            logger.info(String.format("Saving entity to dataset [%s], %d rows affected.", wrapper.describeEntity().getSource(), result));
        } catch (SQLException e) {
            logger.error(String.format("Exception saving new entity to dataset [%s].", wrapper.describeEntity().getSource()), e);
        }
    }

    @Override
    public void saveEntities(EntityDescriber describer, List<EntityWrapper> wrappers) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notEmpty(wrappers, "Wrappers must contains at least one element.");
        if (!checkSourceExists(describer.getSource())) createSource(describer);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            for (final EntityWrapper wrapper : wrappers) {
                String query = sqlInsertQuery(wrapper);
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

    }

    private boolean checkSourceExists(String name) {
        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery(sqlCheckTableExistsQuery(name));
            return resultSet.isFirst() && resultSet.isLast();
        } catch (SQLException e) {
            return true;
        }
    }

    private void createSource(EntityDescriber describer) {
    }

    private String sqlSelectQuery(EntityDescriber describer) {
        StringJoiner joiner = new StringJoiner(FIELD_VALUE_DELIMITER);
        describer.getFields().forEach(f -> joiner.add(f.getName()));
        return String.format("SELECT %s FROM %s", joiner.toString(), describer.getSource());
    }

    private String sqlInsertQuery(EntityWrapper wrapper) {
        StringJoiner fieldJoiner = new StringJoiner(FIELD_VALUE_DELIMITER);
        StringJoiner valuesJoiner = new StringJoiner(FIELD_VALUE_DELIMITER);
        return String.format("INSERT INTO %s (%s) VALUES (%s)", wrapper.describeEntity().getSource(), fieldJoiner.toString(), valuesJoiner.toString());
    }

    private String sqlUpdateQuery(EntityWrapper wrapper) {
        return null;
    }

    private String sqlCheckTableExistsQuery(String name) {
        return String.format("SHOW tables LIKE \'%s\'", name);
    }

    private String sqlCreateTableQuery(EntityDescriber describer) {
        return null;
    }
}
