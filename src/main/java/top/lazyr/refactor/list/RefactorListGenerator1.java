package top.lazyr.refactor.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lazyr.model.component.Graph;
import top.lazyr.model.component.Node;
import top.lazyr.model.manager.GraphManager;
import top.lazyr.refactor.atom.Generator;
import top.lazyr.util.ArrayUtil;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lazyr
 * @created 2022/1/28
 */
public class RefactorListGenerator1 extends RefactorListGenerator {
    private static Logger logger = LoggerFactory.getLogger(RefactorListGenerator1.class);
    private Graph graph;
    private List<Node> smellComponentNodes;
    /**
     * key表示可移动的文件Node
     * value表示可移动的组件(不包含文件Node本身所在的节点)
     */
    private Map<Node, List<Node>> moveFile;
    /**
     * 保存待重构的文件
     * 可每次随机生成下标来选取待重构的文件
     */
    private List<Node> refactoredFileNodes;

    public RefactorListGenerator1(Graph graph, List<Node> smellComponentNodes) {
        this.graph = graph;
        this.smellComponentNodes = smellComponentNodes;
        initMoveFile();
        System.out.println("可重构的文件个数: " + refactoredFileNodes.size());
    }

    @Override
    public String generateOne(int r) {
        // 随机获取待重构文件
        int size = refactoredFileNodes.size();
        int refactoredIndex = ThreadLocalRandom.current().nextInt(size);
        Node refactoredNode = this.refactoredFileNodes.get(refactoredIndex);
        // 获取重构文件所在的组件
        Node sourceComponentNode = GraphManager.findBelongComponent(graph, refactoredNode);
        // 随机获取要移动的目标组件
        List<Node> movedComponentNodes = moveFile.get(refactoredNode);
//        System.out.println(refactoredNode  + " => " + movedComponentNodes.size());
        int movedIndex = ThreadLocalRandom.current().nextInt(movedComponentNodes.size());
        Node targetComponentNode = movedComponentNodes.get(movedIndex);

        return Generator.moveFile(sourceComponentNode.getId(), refactoredNode.getId(), targetComponentNode.getId());

    }

    @Override
    public String generateOne(List<String> refactors, int r) {
        return generateOne(r);
    }

    @Override
    public List<String> generateList(int r) {
        List<String> refactors = new ArrayList<>();
        for (int i = 0; i < r; i++) {
            String refactor = generateOne(r);
            while (!refactorValidate(refactors, refactor)) { // 若无效，则继续随机生成，直到有效为止
                refactor = generateOne(r);

            }
            refactors.add(refactor);
        }
        return refactors;
    }

    @Override
    public String getIntroduction() {
        return "static moveFile";
    }

    private void initMoveFile() {
        this.moveFile = new HashMap<>();
        this.refactoredFileNodes = new ArrayList<>();

        if (this.smellComponentNodes == null) {
            return;
        }

        // 初始化异味文件Node可移动的组件
        for (Node smellComponentNode : smellComponentNodes) {
            // 获取异味组件节点下的文件节点
            List<Node> subFileNodes = GraphManager.findSubFileNodes(graph, smellComponentNode);
            if (subFileNodes == null) {
                continue;
            }
            // 获取每个异味文件节点的系统内传入传出依赖组件
            for (Node subFileNode : subFileNodes) {
                Set<Node> movedComponentNodes = new HashSet<>();
                // 获取依赖subFileNode的所有组件（一定是系统内组件）
                Set<Node> afferentComponentNode = GraphManager.findAfferentComponentNodes(graph, subFileNode);
                if (afferentComponentNode != null) {
                    movedComponentNodes.addAll(afferentComponentNode);
                }
                // 获取subFileNode依赖的所有系统内组件
                Set<Node> systemEfferentComponentNodes = GraphManager.findSystemEfferentComponentNodes(graph, subFileNode);
                if (systemEfferentComponentNodes != null) {
                    movedComponentNodes.addAll(systemEfferentComponentNodes);
                }
                // 不用自己依赖自己
                movedComponentNodes.remove(smellComponentNode);
                if (movedComponentNodes.size() > 0) {
                    moveFile.put(subFileNode, ArrayUtil.set2List(movedComponentNodes));
                }
            }

        }

        // 初始化传入组件中文件可移动到的异味组件
        Set<Node> afferentComponentNodes = new HashSet<>();
        for (Node smellComponentNode : smellComponentNodes) {
            List<Node> tempAfferentComponentNodes = GraphManager.findAfferentNodes(graph, smellComponentNode);
            if (tempAfferentComponentNodes == null) {
                continue;
            }
            afferentComponentNodes.addAll(tempAfferentComponentNodes);
        }
        for (Node afferentComponentNode : afferentComponentNodes) {
            if (smellComponentNodes.indexOf(afferentComponentNode) != -1) { // 上面已处理过异味组件，这里不再处理
                continue;
            }
            List<Node> subFileNodes = GraphManager.findSubFileNodes(graph, afferentComponentNode);
            if (subFileNodes == null) {
                continue;
            }
            for (Node subFileNode : subFileNodes) {
                Set<Node> efferentComponentNodes = GraphManager.findEfferentComponentNodes(graph, subFileNode);
                if (efferentComponentNodes == null) {
                    continue;
                }
                Set<Node> movedComponentNodes = new HashSet<>();
                for (Node smellComponentNode : smellComponentNodes) {
                    if (efferentComponentNodes.contains(smellComponentNode)) {
                        movedComponentNodes.addAll(smellComponentNodes);
                    }
                }
                // 若afferentComponentNode也为异味组件，则可能会自己移动到自己中（获取传出组件是包括自己的）
                movedComponentNodes.remove(afferentComponentNode);
                if (movedComponentNodes.size() > 0) {
                    moveFile.put(subFileNode, ArrayUtil.set2List(movedComponentNodes));
                }
            }
        }

        // 初始化系统内传出组件中文件可移动到的异味组件
        Set<Node> systemEfferentComponentNodes = new HashSet<>();
        for (Node smellComponentNode : smellComponentNodes) {
            List<Node> tempSystemEfferentComponentNodes = GraphManager.findSystemEfferentNodes(graph, smellComponentNode);
            if (tempSystemEfferentComponentNodes == null) {
                continue;
            }
            systemEfferentComponentNodes.addAll(tempSystemEfferentComponentNodes);
        }
        for (Node systemEfferentComponentNode : systemEfferentComponentNodes) {
            if (smellComponentNodes.indexOf(systemEfferentComponentNode) != -1) { // 上面已处理过异味组件，这里不再处理
                continue;
            }
            List<Node> subFileNodes = GraphManager.findSubFileNodes(graph, systemEfferentComponentNode);
            if (subFileNodes == null) {
                continue;
            }
            for (Node subFileNode : subFileNodes) {
                Set<Node> systemAfferentComponentNodes = GraphManager.findAfferentComponentNodes(graph, subFileNode);
                if (systemAfferentComponentNodes == null) {
                    continue;
                }
                Set<Node> movedComponentNodes = null;
                if (moveFile.containsKey(subFileNode)) { // 若已作为传入组件处理，则在之前的数据基础上继续拓展
                    movedComponentNodes = new HashSet<>(moveFile.get(subFileNode));
                } else {
                    movedComponentNodes = new HashSet<>();
                }

                for (Node smellComponentNode : smellComponentNodes) {
                    if (systemAfferentComponentNodes.contains(smellComponentNode)) {
                        movedComponentNodes.add(smellComponentNode);
                    }
                }
                // 若systemEfferentComponentNode也为异味组件，则可能会自己移动到自己中（获取传入组件是包括自己的）
                movedComponentNodes.remove(systemEfferentComponentNode);
                if (movedComponentNodes.size() > 0) {
                    moveFile.put(subFileNode, ArrayUtil.set2List(movedComponentNodes));
                }
            }

        }

        // 初始化可重构的文件Node
        for (Node refactorNode : moveFile.keySet()) {
            this.refactoredFileNodes.add(refactorNode);
        }

    }
}
