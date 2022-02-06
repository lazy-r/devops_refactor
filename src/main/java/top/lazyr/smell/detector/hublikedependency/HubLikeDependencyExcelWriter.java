package top.lazyr.smell.detector.hublikedependency;

import top.lazyr.constant.ConsoleConstant;
import top.lazyr.model.component.Node;
import top.lazyr.util.ExcelUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lazyr
 * @created 2022/1/28
 */
public class HubLikeDependencyExcelWriter {

    public static void write(Map<Node, List<Integer>> smellInfo, String filePath) {
        List<List<String>> infos = new ArrayList<>();
        infos.add(ExcelUtil.generateTitle("异味节点", "传入依赖数", "传出依赖数"));
        for (Node node : smellInfo.keySet()) {
            List<String> info = new ArrayList<>();
            info.add(node.getName());
            info.add(node.getAfferentNum() + "");
            info.add(node.getEfferentNum() + "");
            infos.add(info);
        }
        ExcelUtil.write2Excel(filePath, "info", infos);
    }
}
