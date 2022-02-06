package top.lazyr.genetic.nsgaii.objectivefunction;

import org.junit.Test;
import top.lazyr.genetic.chart.soultionframe.SolutionPanel;
import top.lazyr.model.component.Graph;
import top.lazyr.model.component.Node;
import top.lazyr.model.parser.Parser;
import top.lazyr.model.parser.SourceCodeParser;

import java.util.Arrays;
import java.util.List;

public class CohesionTest {
    @Test
    public void cohesion() {
        String path = "/Users/lazyr/Work/projects/devops/test/data/refactor_demo/target/classes";
        Parser parser = new SourceCodeParser();
        Graph graph = parser.parse(path);
        List<Node> systemComponentNodes = graph.filterSystemComponentNodes();
        Cohesion cohesion = new Cohesion();
        for (Node systemComponentNode : systemComponentNodes) {
            double v = cohesion.calCohesion(graph, systemComponentNode);
            System.out.println(v);
        }
        new SolutionPanel(graph, graph, Arrays.asList());
    }
}
