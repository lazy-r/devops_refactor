package top.lazyr.refactor.list;

import junit.framework.TestCase;
import top.lazyr.model.component.Graph;
import top.lazyr.model.component.Node;
import top.lazyr.model.parser.SourceCodeParser;
import top.lazyr.smell.detector.cyclicdependency.CyclicDependencyDetector;

import java.util.List;

public class RefactorList1Test extends TestCase {

    public void testGenerateOne() {
        String path = "/Users/lazyr/Work/projects/devops/test/data/refactor_demo/target/classes";
        Graph graph = new SourceCodeParser().parse(path);
        CyclicDependencyDetector cdDetector = new CyclicDependencyDetector();
        List<Node> cdNodes = cdDetector.detect(graph);
        RefactorListGenerator refactorList = new RefactorListGenerator1(graph, cdNodes);
        for (int i = 0; i < 10; i++) {
            System.out.println(refactorList.generateOne(10));
        }
    }
}
