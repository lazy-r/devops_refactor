package top.lazyr.smell.detector.cyclicdependency;

import top.lazyr.constant.ConsoleConstant;
import top.lazyr.model.component.Node;

import java.util.List;

/**
 * @author lazyr
 * @created 2022/1/28
 */
public class CyclicDependencyConsoleWriter {

    public static void write(List<Node> smellNodes) {
        ConsoleConstant.printTitle("环依赖异味数: " + smellNodes.size());
        for (Node smellNode : smellNodes) {
            System.out.println(smellNode);
        }
    }
}
