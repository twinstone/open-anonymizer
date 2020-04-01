package application.datasource;

import application.model.describer.EntityDescriber;
import application.model.describer.FieldDescriber;
import application.model.describer.RelationFieldDescriber;
import application.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.StringJoiner;

public class SqlUtils {

    public static final String FIELD_VALUE_DELIMITER = ", ";

    public static String sqlInsertQuery(EntityWrapper wrapper, String delimiter) {
        StringJoiner fieldJoiner = new StringJoiner(delimiter);
        StringJoiner valuesJoiner = new StringJoiner(delimiter);
        List<String> fields = wrapper.describeEntity().getFieldNames();
        fields.forEach(field -> {
            fieldJoiner.add(field);
            valuesJoiner.add(wrapper.getValue(field).toString());
        });
        return String.format("INSERT INTO %s (%s) VALUES (%s);", wrapper.describeEntity().getSource(), fieldJoiner.toString(), valuesJoiner.toString());
    }

    public static String sqlSelectQuery(EntityDescriber describer) {
        StringJoiner joiner = new StringJoiner(SqlUtils.FIELD_VALUE_DELIMITER);
        describer.getFields().forEach(f -> joiner.add(f.getName()));
        return String.format("SELECT %s FROM %s;", joiner.toString(), describer.getSource());
    }

    public static String sqlUpdateQuery(EntityWrapper wrapper) {
        StringJoiner valuesJoiner = new StringJoiner(SqlUtils.FIELD_VALUE_DELIMITER);
        List<String> fields = wrapper.describeEntity().getFieldNames();
        fields.forEach(field -> {
            valuesJoiner.add(String.format("%s=%s", field, wrapper.getValue(field).toString()));
        });
        return String.format("UPDATE %s SET %s WHERE %s;", wrapper.describeEntity().getSource(), valuesJoiner.toString(), String.format("%s=%s", wrapper.describeEntity().getId(), wrapper.getId().toString()));
    }

    public static String sqlCreateTable(EntityDescriber describer) {
        StringJoiner columnJoiner = new StringJoiner(",\n");
        if (StringUtils.isEmpty(describer.getId())) {
            columnJoiner.add("`id` INT NOT NULL");
        }
        for (final FieldDescriber field : describer.getFields()) {
            StringBuilder column = new StringBuilder();
            column.append('`');
            column.append(field.getName());
            column.append('`');
            if (!field.allowsNull()) {
                column.append(" NOT NULL");
            }
            if (field.isUnique()) {
                column.append(" UNIQUE");
            }
            if (!field.isUnique() && StringUtils.isNotEmpty(field.getDefaultValue())) {
                column.append(" DEFAULT ");
                column.append('`');
                column.append(field.getDefaultValue());
                column.append('`');
            }
            columnJoiner.add(column.toString());
        }
        for (final RelationFieldDescriber field : describer.getManyToOneRelationFields()) {
            columnJoiner.add(String.format("`%s` INT NOT NULL", field.getName()));
        }
        if (StringUtils.isEmpty(describer.getId())) {
            columnJoiner.add("PRIMARY KEY (`id`)");
        }
        for (final RelationFieldDescriber field : describer.getManyToOneRelationFields()) {
            columnJoiner.add(String.format("FOREIGN KEY (`%s`) REFERENCES `%s`(`%s`)", describer.getSource() + "_" + field.getName(), describer.getSource(), field.getName()));
        }
        return String.format("CREATE TABLE IF NOT EXISTS `%s` (%s);", describer.getSource(), columnJoiner.toString());
    }

    public static String sqlCheckTableExistsQuery(String name) {
        return String.format("SHOW tables LIKE \'%s\'", name);
    }
}
