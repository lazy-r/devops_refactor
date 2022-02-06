package top.lazyr.smell.detector.unstabledependency;

import top.lazyr.constant.ConsoleConstant;
import top.lazyr.model.component.Node;
import top.lazyr.smell.metrics.ComponentMetricsCalculator;
import top.lazyr.util.ExcelUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lazyr
 * @created 2022/1/28
 */
public class UnstableDependencyExcelWriter {


    public static void write(Map<Node, List<Node>> smellInfo, String filePath) {
        List<List<String>> infos = new ArrayList<>();
        infos.add(ExcelUtil.generateTitle("异味节点", "不稳定指标", "不稳定依赖数", "总依赖数", "不稳定依赖节点"));
        for (Node node : smellInfo.keySet()) {
            List<String> info = new ArrayList<>();
            List<Node> dependNodes = smellInfo.get(node);
            info.add(node.getName());
            info.add(ComponentMetricsCalculator.calInstability(node) + "");
            info.add(dependNodes.size() + "");
            info.add(node.getEfferentNum() + "");
            StringBuilder dependNodeInfo = new StringBuilder();
            for (int i = 0; i < dependNodes.size(); i++) {
                dependNodeInfo.append(dependNodes.get(i).getName() + " " + ComponentMetricsCalculator.calInstability(dependNodes.get(i)));
                if (i != dependNodes.size() - 1) {
                    dependNodeInfo.append("\n");
                }
            }
            info.add(dependNodeInfo.toString());
            infos.add(info);
        }
        ExcelUtil.write2Excel(filePath, "info", infos);
    }
}
