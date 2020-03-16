package application.core.graph;

import application.model.describer.EntityDescriber;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphUtils {

    public static Map<EntityDescriber, List<EntityDescriber>> buildRelationGraph(Map<String, EntityDescriber> describers) {
        Validate.notEmpty(describers, "Describers must be not empty map.");
        Map<EntityDescriber, List<EntityDescriber>> graph = new HashMap<>();
        describers.forEach((source, currentDescriber) -> {
            List<EntityDescriber> relations = new ArrayList<>();
            List<String> names = currentDescriber.getRelationEntitiesNames();
            names.forEach(name -> {
                if (describers.containsKey(name)) {
                    relations.add(describers.get(name));
                } else {
                    throw new IllegalArgumentException(String.format("Describers does not contain describer with name [%s]", name));
                }
            });
            graph.put(currentDescriber, relations);
        });
        return graph;
    }

    public static List<EntityDescriber> topologicalSort(Map<EntityDescriber, List<EntityDescriber>> graph) {
        Validate.notEmpty(graph, "Graph must be not empty.");
        return null;
    }
}
