package openanonymizer.datasource.sql;

import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.wrapper.EntityWrapper;

import java.util.List;
import java.util.StringJoiner;


/**
 * This class allows to generate simple sql queries based on {@link EntityDescriber} content.
 * <p>
 * Use static methods of this class. Do not use constructor.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class SqlUtils {

    public static final String FIELD_VALUE_DELIMITER = ", ";

    /**
     * Generates rows count query.
     *
     * @param describer entity structure
     * @return sql count query
     */
    public static String sqlCountRowsQuery(EntityDescriber describer) {
        return String.format("SELECT COUNT(*) FROM `%s`;", describer.getSource());
    }

    /**
     * Generates select query.
     *
     * @param describer entity structure
     * @return sql select query
     */
    public static String sqlSelectQuery(EntityDescriber describer) {
        StringJoiner joiner = new StringJoiner(SqlUtils.FIELD_VALUE_DELIMITER);
        describer.getFields().forEach(f -> joiner.add(f.getName()));
        return String.format("SELECT %s FROM `%s`;", joiner.toString(), describer.getSource());
    }

    /**
     * Generates select query with skip and limit.
     *
     * @param describer entity structure
     * @param offset    start from row
     * @param limit     max returned rows
     * @return sql select query
     */
    public static String sqlSelectPagedQuery(EntityDescriber describer, long offset, int limit) {
        StringJoiner joiner = new StringJoiner(SqlUtils.FIELD_VALUE_DELIMITER);
        describer.getFields().forEach(f -> joiner.add(f.getName()));
        return String.format("SELECT %s FROM `%s` LIMIT %s, %s;", joiner.toString(), describer.getSource(), offset, limit);
    }

    /**
     * Generates insert query based on wrapper content.
     *
     * @param wrapper entity container
     * @return sql insert query
     */
    public static String sqlInsertQuery(EntityWrapper wrapper) {
        StringJoiner fieldJoiner = new StringJoiner(SqlUtils.FIELD_VALUE_DELIMITER);
        StringJoiner valuesJoiner = new StringJoiner(SqlUtils.FIELD_VALUE_DELIMITER);
        List<String> fields = wrapper.describeEntity().getFieldNames();
        fields.forEach(field -> {
            fieldJoiner.add(String.format("`%s`", field));
            valuesJoiner.add(String.format("'%s'", wrapper.getValue(field).toString()));
        });
        return String.format("INSERT INTO `%s` (%s) VALUES (%s);", wrapper.describeEntity().getSource(), fieldJoiner.toString(), valuesJoiner.toString());
    }

    /**
     * Generates query for updating values in row based on wrapper content.
     *
     * @param wrapper entity container
     * @return sql update query
     */
    public static String sqlUpdateQuery(EntityWrapper wrapper) {
        StringJoiner valuesJoiner = new StringJoiner(SqlUtils.FIELD_VALUE_DELIMITER);
        List<String> fields = wrapper.describeEntity().getFieldNames();
        fields.forEach(field -> {
            valuesJoiner.add(String.format("%s=%s", field, wrapper.getValue(field).toString()));
        });
        return String.format("UPDATE `%s` SET %s WHERE %s;", wrapper.describeEntity().getSource(), valuesJoiner.toString(), String.format("%s=%s", wrapper.describeEntity().getId(), wrapper.getId().toString()));
    }

    /**
     * Generates query to show tables by name .
     *
     * @param name of the table
     * @return sql show query
     */
    public static String sqlCheckTableExistsQuery(String name) {
        return String.format("SHOW tables LIKE `%s`", name);
    }

    private SqlUtils() {
        throw new IllegalStateException();
    }
}
