package top.lazyr.model.writer;

import top.lazyr.constant.ConsoleConstant;
import top.lazyr.model.component.Edge;
import top.lazyr.model.component.Graph;
import top.lazyr.model.component.Node;

import java.util.List;


/**
 * @author lazyr
 * @created 2022/1/27
 */
public class ConsoleModelWriter{

    public static void write(Graph graph) {
        List<Node> fileNodes = graph.getFileNodes();
//        ConsoleConstant.printTitle("FILES");
//        for (Node fileNode : fileNodes) {
//            System.out.println(fileNode.getId());
//            List<Edge> dependEdges = fileNode.getDependEdges();
//            for (Edge dependEdge : dependEdges) {
//                System.out.println("\t\t" + dependEdge.getInNodeName() + "(" + graph.findNodeById(dependEdge.getInNodeId()).getFrom()  + ") ==" + dependEdge.getType() + "==>" + dependEdge.getOutNodeName()  + "(" + graph.findNodeById(dependEdge.getOutNodeId()).getFrom()  + ")");
//            }
//            Edge belongEdge = fileNode.getBelongEdge();
//            System.out.println("\t\t" + belongEdge.getInNodeName() + "(" + graph.findNodeById(belongEdge.getInNodeId()).getFrom()  + ") ==" + belongEdge.getType() + "==> " + belongEdge.getOutNodeName()  + "(" + graph.findNodeById(belongEdge.getOutNodeId()).getFrom()  + ")");
//        }

        ConsoleConstant.printTitle("COMPONENTS");
        List<Node> componentNodes = graph.getComponentNodes();
        for (Node componentNode : componentNodes) {
            System.out.println(componentNode.getId());
            List<Edge> dependEdges = componentNode.getDependEdges();
            for (Edge dependEdge : dependEdges) {
                System.out.println("\t\t" + dependEdge.getInNodeName() + "(" + graph.findNodeById(dependEdge.getInNodeId()).getFrom()  + ") ==" + dependEdge.getType() + "==> " + dependEdge.getOutNodeName()  + "(" + graph.findNodeById(dependEdge.getOutNodeId()).getFrom()  + ")");
            }
        }
    }
}
