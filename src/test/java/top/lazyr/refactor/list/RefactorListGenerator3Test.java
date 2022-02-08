package top.lazyr.refactor.list;

import junit.framework.TestCase;
import top.lazyr.model.component.Graph;
import top.lazyr.model.component.Node;
import top.lazyr.model.parser.SourceCodeParser;
import top.lazyr.smell.detector.cyclicdependency.CyclicDependencyDetector;
import top.lazyr.smell.detector.unstabledependency.UnstableDependencyDetector;

import java.util.*;

public class RefactorListGenerator3Test extends TestCase {

    public void testGenerateOne() {
        String path = "/Users/lazyr/Work/projects/devops/test/data/refactor_demo/target/classes";
        Graph graph = new SourceCodeParser().parse(path);
        Set<Node> udNodes = UnstableDependencyDetector.detect(graph).keySet();
        Set<Node> cdNodes = new HashSet<>(CyclicDependencyDetector.detect(graph));
        Set<Node> smellNodes = new HashSet<>();
        smellNodes.addAll(udNodes);
        smellNodes.addAll(cdNodes);
        RefactorListGenerator3 refactorListGenerator = new RefactorListGenerator3(graph, new ArrayList<>(smellNodes));
        List<String> refactors = new ArrayList<>();
        refactors.add("moveFile,a.p3 ~ COMPONENT ~ SYSTEM,a.p3.C31 ~ FILE ~ SYSTEM,a.p1 ~ COMPONENT ~ SYSTEM");
        refactors.add("moveFile,a.p3 ~ COMPONENT ~ SYSTEM,a.p3.C32 ~ FILE ~ SYSTEM,a.p1 ~ COMPONENT ~ SYSTEM");
        for (int i = 0; i < 20; i++) {
            String refactor = refactorListGenerator.generateOne(refactors, 4);

        }
    }
}
