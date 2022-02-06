package top.lazyr.model.writer;

import top.lazyr.constant.ConsoleConstant;
import top.lazyr.model.component.Graph;
import top.lazyr.model.component.Node;

import java.util.List;

/**
 * @author lazyr
 * @created 2022/1/27
 */
public class NumConsoleModelWriter{
    public static void write(Graph graph) {
        List<Node> systemFileNodes = graph.filterSystemFileNodes();
        List<Node> nonSystemFileNodes = graph.filterNonSystemFileNodes();
        List<Node> systemComponentNodes = graph.filterSystemComponentNodes();
        List<Node> nonSystemComponentNodes = graph.filterNonSystemComponentNodes();
        System.out.println("项目内的文件个数 : " + (systemFileNodes == null ? 0 : systemFileNodes.size()));
        System.out.println("项目外的文件个数 : " + (nonSystemFileNodes == null ? 0 : nonSystemFileNodes.size()));
        System.out.println("所有文件个数     : " + (graph.getFileNodes() == null ? 0 : graph.getFileNodes().size()));
        System.out.println("项目内的组件个数 : " + (systemComponentNodes == null ? 0 : systemComponentNodes.size()));
        System.out.println("项目外的组件个数 : " + (nonSystemComponentNodes == null ? 0 : nonSystemComponentNodes.size()));
        System.out.println("所有组件个数    : " + (graph.getComponentNodes() == null ? 0 : graph.getComponentNodes().size()));
    }
}
