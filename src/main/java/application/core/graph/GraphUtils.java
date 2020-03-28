package application.core.graph;

import application.model.describer.EntityDescriber;
import application.model.describer.RelationFieldDescriber;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphUtils {

    public static Map<EntityDescriber, List<EntityDescriber>> buildManyToOneRelationGraph(Map<String, EntityDescriber> describers) {
        Validate.notEmpty(describers, "Describers must be not empty map.");
        Map<EntityDescriber, List<EntityDescriber>> graph = new HashMap<>();
        describers.forEach((source, currentDescriber) -> {
            List<EntityDescriber> relations = new ArrayList<>();
            List<RelationFieldDescriber> manyToOneRelations = currentDescriber.getManyToOneRelationFields();
            manyToOneRelations.forEach(r -> {
                if (describers.containsKey(r.getName())) {
                    relations.add(describers.get(r.getName()));
                } else {
                    throw new IllegalArgumentException(String.format("Describers does not contain describer with name [%s]", r.getName()));
                }
            });
            graph.put(currentDescriber, relations);
        });
        return graph;
    }

    public static List<Pair<EntityDescriber, EntityDescriber>> getManyToManyRelationDescribers(List<EntityDescriber> describers) {
        Validate.notEmpty(describers, "Describers must be not empty.");
        return null;
    }
}
