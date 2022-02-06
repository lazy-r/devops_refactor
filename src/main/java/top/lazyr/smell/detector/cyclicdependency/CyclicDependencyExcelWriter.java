package top.lazyr.smell.detector.cyclicdependency;

import top.lazyr.constant.ConsoleConstant;
import top.lazyr.model.component.Node;
import top.lazyr.util.ExcelUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lazyr
 * @created 2022/1/28
 */
public class CyclicDependencyExcelWriter {
    public static void write(List<Node> smellNodes, String filePath) {
        List<List<String>> infos = new ArrayList<>();
        infos.add(ExcelUtil.generateTitle("环依赖节点"));
        for (Node smellNode : smellNodes) {
            List<String> info = new ArrayList<>();
            info.add(smellNode.getName());
            infos.add(info);
        }
        ExcelUtil.write2Excel(filePath, "info", infos);
    }
}
