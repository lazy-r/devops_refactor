package top.lazyr.model.parser;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import top.lazyr.constant.ConsoleConstant;
import top.lazyr.model.component.Graph;
import top.lazyr.model.component.Node;
import top.lazyr.model.manager.GraphManager;
import top.lazyr.model.writer.ConsoleModelWriter;
import top.lazyr.model.writer.ExcelModelWriter;
import top.lazyr.model.writer.NumConsoleModelWriter;
import top.lazyr.model.writer.TestConsoleModelWriter;
import top.lazyr.smell.detector.cyclicdependency.CyclicDependencyDetector;
import top.lazyr.smell.detector.hublikedependency.HubLikeDependencyDetector;
import top.lazyr.smell.detector.unstabledependency.UnstableDependencyConsoleWriter;
import top.lazyr.smell.detector.unstabledependency.UnstableDependencyDetector;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SourceCodeParserTest {

    @Test
    public void diff() {
        String componentNames1 = "org.apache.tools.bzip2; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.rmic; PACKAGE; SYSTEM,org.apache.tools.ant.input; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.extension.resolvers; PACKAGE; SYSTEM,org.apache.tools.ant.util; PACKAGE; SYSTEM,org.apache.tools.ant.loader; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.script; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.cvslib; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.condition; PACKAGE; SYSTEM,org.apache.tools.ant.types.resources.comparators; PACKAGE; SYSTEM,org.apache.tools.ant.filters; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.unix; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.javacc; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.native2ascii; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.jsp; PACKAGE; SYSTEM,org.apache.tools.ant.helper; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.pvcs; PACKAGE; SYSTEM,org.apache.tools.ant.types.spi; PACKAGE; SYSTEM,org.apache.tools.ant.types.selectors; PACKAGE; SYSTEM,org.apache.tools.ant.types.optional.depend; PACKAGE; SYSTEM,org.apache.tools.ant.types.mappers; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.javah; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.email; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.depend.constantpool; PACKAGE; SYSTEM,org.apache.tools.ant.types.optional; PACKAGE; SYSTEM,org.apache.tools.ant.dispatch; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.ejb; PACKAGE; SYSTEM,org.apache.tools.zip; PACKAGE; SYSTEM,org.apache.tools.ant.types.resources.selectors; PACKAGE; SYSTEM,org.apache.tools.ant.listener; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.vss; PACKAGE; SYSTEM,org.apache.tools.tar; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.testing; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.launcher; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.clearcase; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.jlink; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.jsp.compilers; PACKAGE; SYSTEM,org.apache.tools.mail; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.modules; PACKAGE; SYSTEM,org.apache.tools.ant.util.facade; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.extension; PACKAGE; SYSTEM,org.apache.tools.ant.util.optional; PACKAGE; SYSTEM,org.apache.tools.ant.util.java15; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.ccm; PACKAGE; SYSTEM,org.apache.tools.ant.filters.util; PACKAGE; SYSTEM,org.apache.tools.ant.attribute; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.i18n; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.depend; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs; PACKAGE; SYSTEM,org.apache.tools.ant.property; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.net; PACKAGE; SYSTEM,org.apache.tools.ant.util.depend; PACKAGE; SYSTEM,org.apache.tools.ant.types.resources; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.sos; PACKAGE; SYSTEM,org.apache.tools.ant.util.regexp; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.j2ee; PACKAGE; SYSTEM,org.apache.tools.ant; PACKAGE; SYSTEM,org.apache.tools.ant.types.selectors.modifiedselector; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.optional.windows; PACKAGE; SYSTEM,org.apache.tools.ant.types; PACKAGE; SYSTEM,org.apache.tools.ant.taskdefs.compilers; PACKAGE; SYSTEM,java.lang; PACKAGE; NON_SYSTEM,java.io; PACKAGE; NON_SYSTEM,java.util; PACKAGE; NON_SYSTEM,java.util.function; PACKAGE; NON_SYSTEM,java.util.stream; PACKAGE; NON_SYSTEM,java.lang.reflect; PACKAGE; NON_SYSTEM,java.nio.charset; PACKAGE; NON_SYSTEM,java.nio.file; PACKAGE; NON_SYSTEM,org.xml.sax; PACKAGE; NON_SYSTEM,java.text; PACKAGE; NON_SYSTEM,java.security; PACKAGE; NON_SYSTEM,javax.xml.transform.sax; PACKAGE; NON_SYSTEM,java.net; PACKAGE; NON_SYSTEM,javax.xml.parsers; PACKAGE; NON_SYSTEM,javax.xml.transform; PACKAGE; NON_SYSTEM,java.util.zip; PACKAGE; NON_SYSTEM,java.math; PACKAGE; NON_SYSTEM,java.nio.file.attribute; PACKAGE; NON_SYSTEM,java.util.concurrent; PACKAGE; NON_SYSTEM,org.xml.sax.helpers; PACKAGE; NON_SYSTEM,org.w3c.dom; PACKAGE; NON_SYSTEM,java.lang.management; PACKAGE; NON_SYSTEM,java.util.regex; PACKAGE; NON_SYSTEM,java.nio; PACKAGE; NON_SYSTEM,java.nio.channels; PACKAGE; NON_SYSTEM,java.lang.ref; PACKAGE; NON_SYSTEM,org.apache.tools.ant.launch; PACKAGE; NON_SYSTEM,javax.script; PACKAGE; NON_SYSTEM,java.util.jar; PACKAGE; NON_SYSTEM,javax.xml.xpath; PACKAGE; NON_SYSTEM,javax.xml.transform.stream; PACKAGE; NON_SYSTEM,java.time; PACKAGE; NON_SYSTEM,java.sql; PACKAGE; NON_SYSTEM,javax.xml.namespace; PACKAGE; NON_SYSTEM,java.util.spi; PACKAGE; NON_SYSTEM,org.apache.tools.ant.filters; PACKAGE; NON_SYSTEM,org.apache.tools.ant.types; PACKAGE; NON_SYSTEM,org.apache.tools.ant.util; PACKAGE; NON_SYSTEM,org.apache.tools.ant; PACKAGE; NON_SYSTEM";
        String componentNames2 = "org.apache.tools.bzip2; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.rmic; COMPONENT; SYSTEM,org.apache.tools.ant.input; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.extension.resolvers; COMPONENT; SYSTEM,org.apache.tools.ant.util; COMPONENT; SYSTEM,org.apache.tools.ant.loader; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.script; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.cvslib; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.condition; COMPONENT; SYSTEM,org.apache.tools.ant.types.resources.comparators; COMPONENT; SYSTEM,org.apache.tools.ant.filters; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.unix; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.javacc; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.native2ascii; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.jsp; COMPONENT; SYSTEM,org.apache.tools.ant.helper; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.pvcs; COMPONENT; SYSTEM,org.apache.tools.ant.types.spi; COMPONENT; SYSTEM,org.apache.tools.ant.types.selectors; COMPONENT; SYSTEM,org.apache.tools.ant.types.optional.depend; COMPONENT; SYSTEM,org.apache.tools.ant.types.mappers; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.javah; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.email; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.depend.constantpool; COMPONENT; SYSTEM,org.apache.tools.ant.types.optional; COMPONENT; SYSTEM,org.apache.tools.ant.dispatch; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.ejb; COMPONENT; SYSTEM,org.apache.tools.zip; COMPONENT; SYSTEM,org.apache.tools.ant.types.resources.selectors; COMPONENT; SYSTEM,org.apache.tools.ant.listener; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.vss; COMPONENT; SYSTEM,org.apache.tools.tar; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.testing; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.launcher; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.clearcase; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.jlink; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.jsp.compilers; COMPONENT; SYSTEM,org.apache.tools.mail; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.modules; COMPONENT; SYSTEM,org.apache.tools.ant.util.facade; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.extension; COMPONENT; SYSTEM,org.apache.tools.ant.util.optional; COMPONENT; SYSTEM,org.apache.tools.ant.util.java15; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.ccm; COMPONENT; SYSTEM,org.apache.tools.ant.filters.util; COMPONENT; SYSTEM,org.apache.tools.ant.attribute; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.i18n; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.depend; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs; COMPONENT; SYSTEM,org.apache.tools.ant.property; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.net; COMPONENT; SYSTEM,org.apache.tools.ant.util.depend; COMPONENT; SYSTEM,org.apache.tools.ant.types.resources; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.sos; COMPONENT; SYSTEM,org.apache.tools.ant.util.regexp; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.j2ee; COMPONENT; SYSTEM,org.apache.tools.ant; COMPONENT; SYSTEM,org.apache.tools.ant.types.selectors.modifiedselector; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.optional.windows; COMPONENT; SYSTEM,org.apache.tools.ant.types; COMPONENT; SYSTEM,org.apache.tools.ant.taskdefs.compilers; COMPONENT; SYSTEM,java.lang; COMPONENT; NON_SYSTEM,java.io; COMPONENT; NON_SYSTEM,java.util; COMPONENT; NON_SYSTEM,java.util.function; COMPONENT; NON_SYSTEM,java.util.stream; COMPONENT; NON_SYSTEM,java.lang.reflect; COMPONENT; NON_SYSTEM,java.nio.charset; COMPONENT; NON_SYSTEM,java.nio.file; COMPONENT; NON_SYSTEM,org.xml.sax; COMPONENT; NON_SYSTEM,java.text; COMPONENT; NON_SYSTEM,java.security; COMPONENT; NON_SYSTEM,javax.xml.transform.sax; COMPONENT; NON_SYSTEM,java.net; COMPONENT; NON_SYSTEM,javax.xml.transform; COMPONENT; NON_SYSTEM,javax.xml.parsers; COMPONENT; NON_SYSTEM,java.util.zip; COMPONENT; NON_SYSTEM,java.math; COMPONENT; NON_SYSTEM,java.nio.file.attribute; COMPONENT; NON_SYSTEM,java.util.concurrent; COMPONENT; NON_SYSTEM,org.xml.sax.helpers; COMPONENT; NON_SYSTEM,org.w3c.dom; COMPONENT; NON_SYSTEM,java.lang.management; COMPONENT; NON_SYSTEM,java.util.regex; COMPONENT; NON_SYSTEM,java.nio; COMPONENT; NON_SYSTEM,java.nio.channels; COMPONENT; NON_SYSTEM,java.lang.ref; COMPONENT; NON_SYSTEM,org.apache.tools.ant.launch; COMPONENT; NON_SYSTEM,javax.script; COMPONENT; NON_SYSTEM,java.util.jar; COMPONENT; NON_SYSTEM,java.sql; COMPONENT; NON_SYSTEM,javax.xml.xpath; COMPONENT; NON_SYSTEM,javax.xml.transform.stream; COMPONENT; NON_SYSTEM,java.time; COMPONENT; NON_SYSTEM,javax.xml.namespace; COMPONENT; NON_SYSTEM,java.util.spi; COMPONENT; NON_SYSTEM";
        Set<String> componentSet1 = toSet(componentNames1);
        Set<String> componentSet2 = toSet(componentNames2);
        ConsoleConstant.printTitle("新方法缺少的组件");
        for (String componentName : componentSet1) {
            if (!componentSet2.contains(componentName)) {
                System.out.println(componentName);
            }
        }
        ConsoleConstant.printTitle("旧方法缺少的组件");
        for (String componentName : componentSet2) {
            if (!componentSet1.contains(componentName)) {
                System.out.println(componentName);
            }
        }

    }

    private Set<String> toSet(String componentNames) {
        String[] componentNameArr = componentNames.split(",");
        Set<String> set = new HashSet<>();
        for (String componentName : componentNameArr) {
            set.add(componentName);
        }
        return set;
    }

    @Test
    public void testGraph() {
        String path = "/Users/lazyr/Work/projects/devops/test/data/refactor_demo/target/classes";
        Graph graph = new SourceCodeParser().parse(path);
        new ConsoleModelWriter().write(graph);

    }




    @Test
    public void testParse() {
        String[] paths = new String[]{
//                "/Users/lazyr/Work/projects/devops/test/data/refactor_demo/target/classes",
//                "/Users/lazyr/Work/graduation/arcan/data/ant/org",
//                "/Users/lazyr/Work/graduation/arcan/data/zooleeper/org",
                // 有不存在外部类的内部类存在
//                "/Users/lazyr/Work/graduation/arcan/data/storm-core/org",
                // 环数量过多（arcan都检测出两百多个）
//                "/Users/lazyr/Work/graduation/arcan/data/pig/org",
//                "/Users/lazyr/Work/graduation/arcan/data/httpclient/org",
                // 环数量过多（arcan都检测出一百多个）
//                "/Users/lazyr/Work/graduation/arcan/data/hutool/cn",
//                "/Users/lazyr/Work/graduation/arcan/data/eclipse/org",
//                "/Users/lazyr/Work/graduation/arcan/data/tomcat/org",
                // 很棒，无异味
//                "/Users/lazyr/Work/graduation/arcan/data/spring-jdbc/org",
//                "/Users/lazyr/Work/graduation/arcan/data/lucene-core/org",
//                "/Users/lazyr/Work/graduation/arcan/data/struts2-core/org",
//                "/Users/lazyr/Work/graduation/arcan/data/kafka/kafka",
                "/Users/lazyr/Work/graduation/arcan/data/spark-core/org",
//                "/Users/lazyr/Work/graduation/arcan/data/hadoop-common/org",
//                "/Users/lazyr/Work/graduation/arcan/data/dubbo/com",
//                "/Users/lazyr/Work/graduation/arcan/data/jedit/org",
                // 包含kotlin代码
//                "/Users/lazyr/Work/graduation/arcan/data/spring-context/org",
        };
        for (String path : paths) {
//            refactor(path);
            getData(path);
        }
//        String path = "/Users/lazyr/Work/projects/devops/dop/application-server/target/classes/com";


    }

    private void getData(String path) {
        String[] split = path.split("/");
        ConsoleConstant.printTitle(split[split.length - 2]);
        Graph graph = new SourceCodeParser().parse(path);
        new NumConsoleModelWriter().write(graph);
        ConsoleConstant.printTitle("异味信息");

        Map<Node, List<Node>> udSmellInfo = new UnstableDependencyDetector().detect(graph);

        Map<Node, List<Integer>> hlSmellInfo = new HubLikeDependencyDetector().detect(graph);

        List<Node> cdSmellInfo = new CyclicDependencyDetector().detect(graph);


        Set<Node> smellNodes = new HashSet<>();
        int smellFileNum = 0;
        int udSmellFileNum = 0;
        for (Node node : udSmellInfo.keySet()) {
            smellNodes.add(node);
            List<Node> subFileNodes = GraphManager.findSubFileNodes(graph, node);
            udSmellFileNum += subFileNodes.size();
        }
        int hlSmellFileNum = 0;
        for (Node node : hlSmellInfo.keySet()) {
            smellNodes.add(node);
            List<Node> subFileNodes = GraphManager.findSubFileNodes(graph, node);
            hlSmellFileNum += subFileNodes.size();
        }
        int cdSmellFileNum = 0;
        for (Node node : cdSmellInfo) {
            smellNodes.add(node);
            List<Node> subFileNodes = GraphManager.findSubFileNodes(graph, node);
            cdSmellFileNum += subFileNodes.size();
        }

        for (Node node : smellNodes) {
            List<Node> subFileNodes = GraphManager.findSubFileNodes(graph, node);
            smellFileNum += subFileNodes.size();
        }


//        int smellNum = smellNodes.size();
        int smellNum = udSmellInfo.size() + hlSmellInfo.size() + cdSmellInfo.size();
//        int smellNum = udSmellInfo.size() + hlSmellInfo.size();
        System.out.println("不稳定赖异味组件数: " + udSmellInfo.size());
        System.out.println("枢纽型依赖异味组件数: " + hlSmellInfo.size());
        System.out.println("环依赖异味组件数: " + cdSmellInfo.size());
        System.out.println("异味组件总数: " + smellNodes.size());
        System.out.println("不稳定依赖异味文件数: " + udSmellFileNum);
        System.out.println("枢纽型依赖异味文件数: " + hlSmellFileNum);
        System.out.println("环依赖型依赖异味文件数: " + cdSmellFileNum);
        System.out.println("异味文件总数: " + smellFileNum);
        System.out.println("总异味数: " + smellNum);

    }
}
