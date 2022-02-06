package top.lazyr.refactor.action;

import junit.framework.TestCase;
import org.junit.Test;
import top.lazyr.genetic.chart.soultionframe.SolutionPanel;
import top.lazyr.genetic.chart.soultionframe.FileNode;
import top.lazyr.model.component.Graph;
import top.lazyr.model.manager.GraphManager;
import top.lazyr.model.parser.Parser;
import top.lazyr.model.parser.SourceCodeParser;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class RefactorActuatorTest extends TestCase {

    public void testRefactor() {
        String path = "/Users/lazyr/Work/projects/devops/test/data/refactor_demo/target/classes";
        Parser parser = new SourceCodeParser();
        Graph graph = parser.parse(path);

        List<String> refactors = new ArrayList<>();

        refactors.add("moveFile,a.p3 ~ COMPONENT ~ SYSTEM,a.p3.C32 ~ FILE ~ SYSTEM,a.p2 ~ COMPONENT ~ SYSTEM");
        refactors.add("moveFile,a.p3 ~ COMPONENT ~ SYSTEM,a.p3.C33 ~ FILE ~ SYSTEM,a.p1 ~ COMPONENT ~ SYSTEM");
        refactors.add("moveFile,a.p2 ~ COMPONENT ~ SYSTEM,a.p2.C22 ~ FILE ~ SYSTEM,a.p3 ~ COMPONENT ~ SYSTEM");
        refactors.add("moveFile,a.p3 ~ COMPONENT ~ SYSTEM,a.p3.C33 ~ FILE ~ SYSTEM,a.p1 ~ COMPONENT ~ SYSTEM");

        Graph refactoredGraph = GraphManager.cloneGraph(graph);
        int[] refactor = new RefactorActuator().refactor(refactoredGraph, refactors);

//        ConsoleConstant.printTitle("重构前");
//        TestConsoleModelWriter.write(graph, "a.p1 ~ COMPONENT ~ SYSTEM");
//        ConsoleConstant.printTitle("重构后");
//        TestConsoleModelWriter.write(refactoredGraph, "a.p1 ~ COMPONENT ~ SYSTEM");

//        ConsoleConstant.printTitle("成功: " + refactor[0]);
//        ConsoleConstant.printTitle("失败: " + refactor[1]);

        new SolutionPanel(graph, refactoredGraph, refactors);
    }

    @Test
    public void test() {
        FileNode rootNode = FileNode.builder().name("root").completeName("root").build();
        FileNode first11 = FileNode.builder().name("first11").completeName("first11").build();
        rootNode.add(first11);
        FileNode first12 = FileNode.builder().name("first12").completeName("first12").build();
        rootNode.add(first12);
        FileNode first21 = FileNode.builder().name("first21").completeName("first21").build();
        first11.add(first21);
        FileNode first22 = FileNode.builder().name("first22").completeName("first22").build();
        first12.add(first22);
        JTree tree = new JTree(rootNode);
        JFrame f = new JFrame("系统内文件目录");
        f.add(tree);
        f.setSize(300, 300);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
