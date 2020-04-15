package openanonymizer.model.mapper;

import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import openanonymizer.model.wrapper.Neo4jEntityWrapperImpl;
import org.apache.commons.lang3.Validate;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.value.NodeValue;

/**
 * Mapper for Neo4j {@link Record}.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class Neo4jEntityMapper implements EntityWrapperMapper<Record> {

    @Override
    public EntityWrapper getFromEntity(Record entity, EntityDescriber describer) {
        Validate.notNull(entity, "Entity must be not null.");
        Validate.notNull(describer, "Describer must be not null.");
        NodeValue v = (NodeValue) entity.get(0);
        InternalNode node = (InternalNode) v.asNode();
        return new Neo4jEntityWrapperImpl(node.id(), node.asMap(), describer);
    }

    @Override
    public Record getFromWrapper(EntityWrapper wrapper) {
        throw new UnsupportedOperationException();
    }
}
