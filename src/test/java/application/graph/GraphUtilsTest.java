package application.graph;

import application.core.graph.GraphUtils;
import application.model.describer.EntityDescriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphUtilsTest {

    private final static URL PATH = GraphUtilsTest.class.getClassLoader().getResource("graph_test_config.json");

    @Test(expected = NullPointerException.class)
    public void nullDescribersTest() {
        GraphUtils.buildManyToOneRelationGraph(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyDescribersTest() {
        GraphUtils.buildManyToOneRelationGraph(Collections.emptyMap());
    }

    @Test
    public void graphBuildTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        EntityDescriber[] describers = mapper.readValue(PATH, EntityDescriber[].class);
        Map<EntityDescriber, List<EntityDescriber>> graph = GraphUtils.buildManyToOneRelationGraph(Arrays.stream(describers)
                .collect(Collectors.toMap(EntityDescriber::getName, describer -> describer)));

    }

}
