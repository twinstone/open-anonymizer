package openanonymizer.model.wrapper;

import com.google.common.collect.Maps;
import openanonymizer.model.describer.EntityDescriber;

import java.util.Collections;
import java.util.Map;

/**
 * Container for {@link openanonymizer.model.dataset.DataSet} values returned by {@link openanonymizer.datasource.neo4j.Neo4jDataSource}.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class Neo4jEntityWrapperImpl extends EntityWrapperImpl {

    private final Long id;

    public Neo4jEntityWrapperImpl(Long id, Map<String, Object> entityAsMap, EntityDescriber describer) {
        super(entityAsMap, describer);
        this.id = id;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Map<String, Object> getEntityAsMap() {
        Map<String, Object> map = Maps.newHashMap(entityAsMap);
        map.put("id", id);
        return Collections.unmodifiableMap(map);
    }
}
