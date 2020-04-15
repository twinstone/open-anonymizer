package openanonymizer.datasource.neo4j;

import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * This class allows to generate simple cypher queries based on {@link EntityDescriber} content.
 * <p>
 * Use static methods of this class. Do not use constructor.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class Neo4jUtils {

    /**
     * Generates nodes count query.
     *
     * @param describer entity structure
     * @return cql count query
     */
    public static String countEntitiesQuery(EntityDescriber describer) {
        return String.format("MATCH (%s:`%s`) RETURN COUNT(*)", describer.getName(), describer.getSource());
    }

    /**
     * Generates nodes match query.
     *
     * @param describer entity structure
     * @return cql match query
     */
    public static String entityMatchQuery(EntityDescriber describer) {
        return String.format("MATCH (%s:`%s`) RETURN %s;",
                describer.getName(),
                describer.getSource(),
                describer.getName());
    }

    /**
     * Generates nodes match query with offset and limit.
     *
     * @param describer entity structure
     * @param offset    start from node
     * @param limit     max returned nodes
     * @return cql match query
     */
    public static String entityPagedMatchQuery(EntityDescriber describer, long offset, int limit) {
        return String.format("MATCH (%s:`%s`) RETURN %s SKIP %s LIMIT %s;",
                describer.getName(),
                describer.getSource(),
                describer.getName(),
                offset,
                limit);
    }

    /**
     * Generates create node query based on wrapper content.
     *
     * @param wrapper entity container
     * @return cql create query
     */
    public static String entityCreateQuery(EntityWrapper wrapper) {
        EntityDescriber describer = wrapper.describeEntity();
        return String.format("CREATE (%s:%s) SET %s RETURN ID(%s)",
                describer.getName(),
                describer.getSource(),
                getFields(wrapper),
                describer.getName());
    }

    /**
     * Generates update node query based on wrapper content.
     *
     * @param wrapper entity container
     * @return cql update query
     */
    public static String entityUpdateQuery(EntityWrapper wrapper) {
        EntityDescriber describer = wrapper.describeEntity();
        return String.format("MERGE (%s:%s) SET %s WHERE ID(%s) = %s RETURN %s",
                describer.getName(),
                describer.getSource(),
                getFields(wrapper),
                describer.getName(),
                wrapper.getId(),
                describer.getName());
    }

    /**
     * Generates create relation query based on two nodes structure.
     *
     * @param left         entity structure
     * @param right        entity structure
     * @param leftId       left entity id
     * @param rightId      right entity id
     * @param relationName name of the relation
     * @return cql create relation query
     */
    public static String relationCreateQuery(EntityDescriber left, EntityDescriber right, Object leftId, Object rightId, String relationName) {
        return String.format("MATCH (%s:%s), (%s:%s) WHERE ID(%s) = %s AND ID(%s) = %s CREATE (%s)-[r:%s]->(%s) RETURN %s, %s",
                left.getName(),
                left.getSource(),
                right.getName(),
                right.getSource(),
                left.getName(),
                leftId,
                right.getName(),
                rightId,
                left.getName(),
                relationName,
                right.getName(),
                left.getName(),
                right.getName());
    }

    /**
     * Generates update part of query
     *
     * @param wrapper entity content
     * @return part of query that sets new values
     */
    private static String getFields(EntityWrapper wrapper) {
        StringJoiner fieldJoiner = new StringJoiner(", ");
        EntityDescriber describer = wrapper.describeEntity();
        List<String> fields = wrapper.describeEntity()
                .getFields()
                .stream()
                .map(FieldDescriber::getName)
                .collect(Collectors.toList());
        fields.forEach(field -> fieldJoiner.add(String.format("%s.%s = $%s", describer.getName(), field, field)));
        return fieldJoiner.toString();
    }

    private Neo4jUtils() {
        throw new IllegalStateException();
    }
}
