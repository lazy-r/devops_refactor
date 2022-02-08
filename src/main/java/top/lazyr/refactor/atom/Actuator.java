package top.lazyr.refactor.atom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lazyr.constant.NodeConstant;
import top.lazyr.constant.RefactorConstant;
import top.lazyr.model.component.Edge;
import top.lazyr.model.component.Graph;
import top.lazyr.model.component.Node;
import top.lazyr.model.manager.GraphManager;

import java.util.*;

/**
 * @author lazyr
 * @created 2022/1/28
 */
public class Actuator {
    private static Logger logger = LoggerFactory.getLogger(Actuator.class);


    /**
     * moveFile原子操作
     * 根据moveFile字符串(moveFile,sourceComponentNodeId,fileNodeId,targetComponentNodeId)进行操作
     * 返回成功移动文件的个数和失败移动文件的个数
     * [succeedNum, failedNum]
     * @param graph
     * @param moveFile
     * @return
     */
    public static int[] moveFile(Graph graph, String moveFile) {
        // 记录执行成功和失败的个数
        String[] actions = moveFile.split(RefactorConstant.ACTION_SEPARATOR);
        if (!actions[0].equals(RefactorConstant.MOVE_FILE)) {
            return new int[]{0, 0};
        }
        String sourceComponentNodeId = actions[1];
        String fileNodeId = actions[2];
        String targetComponentNodeId = actions[3];

        Node sourceComponentNode = graph.findNodeById(sourceComponentNodeId);
        Node fileNode = graph.findNodeById(fileNodeId);
        Node targetComponentNode = graph.findNodeById(targetComponentNodeId);
        if (!actionValidator(fileNode, sourceComponentNode)) { // 若sourceComponentNode中无fileNode，则认为操作失败
            return new int[]{0, 1};
        }

        moveFile(graph, sourceComponentNode, fileNode, targetComponentNode);
        return new int[]{1, 0};
    }

    /**
     * extractComponent原子操作
     * 根据extractComponent字符串(extractComponent,sourceComponentNodeId1>fileNodeId1;sourceComponentNodeId2>fileNodeId2,from_sourceComponentNodeId)进行操作
     * 返回成功移动文件的个数和失败移动文件的个数
     * [succeedNum, failedNum]
     * @param graph
     * @param extractComponent
     * @return
     */
    public static int[] extractComponent(Graph graph, String extractComponent) {
        int succeed = 0;
        int failed = 0;

        String[] actions = extractComponent.split(RefactorConstant.ACTION_SEPARATOR);
        if (!actions[0].equals(RefactorConstant.EXTRACT_COMPONENT)) { // 若重构操作不是抽取组件，则不进行后续操作
            return new int[]{0, 0};
        }

        String[] componentFileIds = actions[1].split(RefactorConstant.EXTRACT_COMPONENT_SEPARATOR);
        String targetComponentNodeId = actions[2];

        Map<Node, Node> fileComponentNodes = new HashMap<>();
        for (String componentFileId : componentFileIds) {
            String[] componentFile = componentFileId.split(RefactorConstant.BELONG_SEPARATOR);
            String componentId = componentFile[0];
            String fileId = componentFile[1];
            Node componentNode = graph.findNodeById(componentId);
            Node fileNode = graph.findNodeById(fileId);
            if (componentNode == null || fileNode == null) { // 若组件或文件有一个不存在，则认为失败次数加一
                failed++;
                System.out.println("");
                continue;
            }
            fileComponentNodes.put(fileNode, componentNode);
        }


        Node targetComponentNode = graph.findNodeById(targetComponentNodeId);
        if (targetComponentNode == null) { // targetComponentNode未创建, 创建并添加到graph中
            targetComponentNode = GraphManager.buildSystemComponentNode(targetComponentNodeId.split(NodeConstant.SEPARATOR)[0]);
            graph.addComponentNode(targetComponentNode);
        }

        for (Node fileNode : fileComponentNodes.keySet()) {
            Node sourceComponentNode = fileComponentNodes.get(fileNode);

            if (!actionValidator(fileNode, sourceComponentNode)) {
                failed++;
                continue;
            }
            moveFile(graph, sourceComponentNode, fileNode, targetComponentNode);
            succeed++;
        }

        return new int[]{succeed, failed};
    }

    /**
     * 验证重构操作是否合法
     * - 若sourceComponentNode中已不存在fileNode，则认为该重构操作无效
     * @param fileNode
     * @param sourceComponentNode
     * @return
     */
    private static boolean actionValidator(Node fileNode, Node sourceComponentNode) {
        if (fileNode == null || sourceComponentNode == null) {
            return false;
        }
        if (fileNode.isComponent() || !sourceComponentNode.isComponent()) {
            return false;
        }

        Edge belongEdge = fileNode.getBelongEdge();
        if (belongEdge == null) {
            logger.error("file node({}) does not contain belong edge.", fileNode);
            return false;
        }

        return belongEdge.getOutNodeId().equals(sourceComponentNode.getId());
    }


    /**
     * 将graph中 fileNode 从 sourceComponentNode 移动到 targetComponentNode
     * - 获取所有依赖fileNode的文件Node对应的组件，
     *      - 若这些组件只依赖sourceComponentNode中fileNode，则删除依赖sourceComponentNode
     *      - 将这些组件添加依赖targetComponentNode
     * - 在targetComponentNode中添加fileNode依赖的边
     * - 修改fileNode的belong边指向targetComponentNode
     * - 删除 sourceComponentNode 中只有 fileNode 唯一依赖的组件的边
     * - TODO: 若删除完fileNode，componentNode无子类，是否需要删除？
     * @param graph
     * @param targetComponentNode
     * @param fileNode
     */
    private static void moveFile(Graph graph, Node sourceComponentNode, Node fileNode, Node targetComponentNode) {
        if (sourceComponentNode == null || graph == null || fileNode == null || targetComponentNode == null) {
            return;
        }

        // 1、获取所有依赖fileNode的文件Node对应的组件
        Map<Node, Set<Node>> groups = GraphManager.groupAfferentNodesByComponentNode(graph, fileNode);
        if (groups != null) {
            for (Node afferentComponentNode : groups.keySet()) {
                // 1.1 若这些组件只依赖sourceComponentNode中fileNode，则删除依赖sourceComponentNode
                Set<Node> fileNodes = groups.get(afferentComponentNode);
                if (fileNodes.size() == 1) {
                    graph.removeDepend(afferentComponentNode, sourceComponentNode);
                }
                // 1.2 将这些组件添加依赖targetComponentNode
                graph.addDepend(afferentComponentNode, targetComponentNode);
            }
        }


        // 2、在targetComponentNode中添加fileNode依赖的组件
        Set<Node> efferentComponentNodes = GraphManager.findEfferentComponentNodes(graph, fileNode);
        if (efferentComponentNodes != null) {
            for (Node efferentComponentNode : efferentComponentNodes) {
                graph.addDepend(targetComponentNode, efferentComponentNode);
            }
        }

        // 3、修改 fileNode 中belong边指向 targetComponentNode
        graph.updateBelong(fileNode, targetComponentNode);


        // 4、删除 sourceComponentNode 中只有 fileNode 唯一依赖的组件的边
        // 获取fileNode依赖的组件Node(并删除sourceComponentNode)
//        new TestConsoleModelWriter(fileNode.getId()).write(graph);
        Set<Node> dependComponentNodes = GraphManager.findEfferentComponentNodes(graph, fileNode);
        if (dependComponentNodes == null) { // 若无依赖的组件，则无需做后续处理
            return;
        }
        dependComponentNodes.remove(sourceComponentNode);


        // 获取componentNode中只有fileNode依赖的组件Node
        List<Node> subFileNodes = GraphManager.findSubFileNodes(graph, sourceComponentNode);
        if (subFileNodes != null) {
            for (Node subFileNode : subFileNodes) {
                Set<Node> subDependComponentNodes = GraphManager.findEfferentComponentNodes(graph, subFileNode);
                if (subDependComponentNodes == null) {
                    continue;
                }
                for (Node subDependComponentNode : subDependComponentNodes) {
                    dependComponentNodes.remove(subDependComponentNode);
                }
            }
        }

        // 删除sourceComponentNode中只有fileNode依赖的组件Node
        for (Node dependComponentNode : dependComponentNodes) {
            graph.removeDepend(sourceComponentNode, dependComponentNode);
        }

    }
}
