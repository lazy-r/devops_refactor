package top.lazyr.smell.detector.hublikedependency;

import top.lazyr.constant.ConsoleConstant;
import top.lazyr.model.component.Node;

import java.util.List;
import java.util.Map;

/**
 * @author lazyr
 * @created 2022/1/27
 */
public class HubLikeDependencyConsoleWriter {
    public static void write(Map<Node, List<Integer>> smellInfo) {
        ConsoleConstant.printTitle("枢纽型异味组件个数: " + smellInfo.size());
        for (Node node : smellInfo.keySet()) {
            System.out.println(node + ", 传入依赖数: " + node.getAfferentNum() + ", 传出依赖数: " + node.getEfferentNum());
        }
    }
}
