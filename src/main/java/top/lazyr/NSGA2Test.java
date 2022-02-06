package top.lazyr;

import top.lazyr.constant.ConsoleConstant;
import top.lazyr.genetic.chart.soultionframe.SolutionFrame;
import top.lazyr.genetic.nsgaii.Configuration;
import top.lazyr.genetic.nsgaii.NSGA2;
import top.lazyr.genetic.nsgaii.model.chromosome.Chromosome;
import top.lazyr.genetic.nsgaii.model.chromosome.RefactorChromosome;
import top.lazyr.genetic.nsgaii.model.population.Population;
import top.lazyr.genetic.nsgaii.mutation.AbstractMutation;
import top.lazyr.genetic.nsgaii.mutation.SingleMutation;
import top.lazyr.genetic.nsgaii.objectivefunction.*;
import top.lazyr.genetic.nsgaii.plugin.childpopulationproducer.*;
import top.lazyr.genetic.nsgaii.plugin.geneticcodeproducer.*;
import top.lazyr.genetic.nsgaii.plugin.populationproducer.*;
import top.lazyr.genetic.nsgaii.selector.*;
import top.lazyr.genetic.nsgaii.termination.*;
import top.lazyr.genetic.nsgaii.writer.SolutionWriter;
import top.lazyr.model.component.Graph;
import top.lazyr.model.component.Node;
import top.lazyr.model.manager.GraphManager;
import top.lazyr.model.parser.Parser;
import top.lazyr.model.parser.SourceCodeParser;
import top.lazyr.model.writer.ExcelModelWriter;
import top.lazyr.model.writer.NumConsoleModelWriter;
import top.lazyr.refactor.list.RefactorListGenerator;
import top.lazyr.refactor.list.RefactorListGenerator1;
import top.lazyr.refactor.list.RefactorListGenerator2;
import top.lazyr.smell.detector.cyclicdependency.CyclicDependencyDetector;
import top.lazyr.smell.detector.cyclicdependency.CyclicDependencyExcelWriter;
import top.lazyr.smell.detector.hublikedependency.HubLikeDependencyDetector;
import top.lazyr.smell.detector.hublikedependency.HubLikeDependencyExcelWriter;
import top.lazyr.smell.detector.unstabledependency.UnstableDependencyDetector;
import top.lazyr.smell.detector.unstabledependency.UnstableDependencyExcelWriter;
import top.lazyr.genetic.nsgaii.crossover.*;
import top.lazyr.util.FileUtil;
import top.lazyr.util.ObjectiveUtil;

import java.util.*;

/**
 * @author lazyr
 * @created 2022/1/28
 */
public class NSGA2Test {
    /* 种群大小 */
    static int populationSize = 40;
    /* 演进代数 */
    static int generationCount = 100;
    /* 基因型长度 */
    static int chromosomeLength = 30;
    /* 变异概率 */
    static float mutationProbability = 0.8f;
    /* 交叉概率 */
    static float crossoverProbability = 0.8f;
    /* 是否重构枢纽型异味（三种异味至少包含一种） */
    static boolean refactorHL = true;
    /* 是否重构不稳定依赖异味（三种异味至少包含一种） */
    static boolean refactorUD = true;
    /* 是否重构环依赖异味（三种异味至少包含一种） */
    static boolean refactorCD = true;
    /* 是否优化耦合指标 */
    static boolean optimizedCoupling = true;
    /* 是否优化内聚指标 */
    static boolean optimizedCohesion = true;
    /* 重构操作列表生成方案: 1表示使用RefactorListGenerator1生成、2表示使用RefactorListGenerator2生成 */
    private static int refactorListGeneratorNum = 1;
    /* 输出目录前缀 */
    private static String prefix = "exp2/";

    public static void main(String[] args) {
        // 重构项目绝对路径
        String[] paths = new String[]{
//                "/Users/lazyr/Work/projects/devops/test/data/refactor_demo/target/classes",
//                "/Users/lazyr/Work/graduation/arcan/data/jedit/org",
//                "/Users/lazyr/Work/graduation/arcan/data/ant/org",
//                "/Users/lazyr/Work/graduation/arcan/data/pig/org",
//                "/Users/lazyr/Work/graduation/arcan/data/hutool/cn",
//                "/Users/lazyr/Work/graduation/arcan/data/eclipse/org",
                "/Users/lazyr/Work/graduation/arcan/data/lucene-core/org",
//                "/Users/lazyr/Work/graduation/arcan/data/spark-core/org",
//                "/Users/lazyr/Work/graduation/arcan/data/hadoop-common/org",
//                "/Users/lazyr/Work/graduation/arcan/data/dubbo/com",
        };
        for (String path : paths) {
            refactor(path);
//            parse(path);
        }
    }

    public static void refactor(String path) {
        if (!(refactorCD || refactorHL || refactorUD)) {
            System.out.println("至少选择一种异味进行重构");
            return;
        }
        FileUtil.clearDir(prefix);

        // 1、构建源代码模型图并输出到 "resources/exp/源代码模型图.xlsx"
        Parser parser = new SourceCodeParser();
        Graph orignGraph = parser.parse(path);
        // 输出源代码模型图信息到 "exp2/源代码模型图.xlsx"
        ExcelModelWriter.write(orignGraph, prefix + "源代码模型图.xlsx");
        ConsoleConstant.printTitle("项目基础信息");
        NumConsoleModelWriter.write(orignGraph);

        // 2、异味检测并输出信息
        ConsoleConstant.printTitle("异味信息");
        int smellNum = 0; // 记录异味个数
        int smellFileNum = 0;
        Set<Node> smellComponentNodes = new HashSet<>(); // 记录异味组件Node
        if (refactorUD) {
            // 输出不稳定依赖异味信息到 "exp2/不稳定依赖异味.xlsx"
            Map<Node, List<Node>> udSmellInfo = UnstableDependencyDetector.detect(orignGraph);
            UnstableDependencyExcelWriter.write(udSmellInfo, prefix + "不稳定依赖异味.xlsx");
            System.out.println("不稳定依赖异味个数: " + udSmellInfo.size());
            smellNum += udSmellInfo.size();
            smellFileNum += findSubFiles(orignGraph, udSmellInfo.keySet());
            smellComponentNodes.addAll(udSmellInfo.keySet());
        }


        if (refactorHL) {
            // 输出枢纽型异味信息到 "exp2/枢纽型依赖异味.xlsx"
            Map<Node, List<Integer>> hlSmellInfo = HubLikeDependencyDetector.detect(orignGraph);
            HubLikeDependencyExcelWriter.write(hlSmellInfo, prefix + "枢纽型依赖异味.xlsx");
            System.out.println("枢纽型依赖异味个数: " + hlSmellInfo.size());
            smellNum += hlSmellInfo.size();
            smellFileNum += findSubFiles(orignGraph, hlSmellInfo.keySet());
            smellComponentNodes.addAll(hlSmellInfo.keySet());
        }

        if (refactorCD) {
            // 输出环依赖异味信息到 "exp2/环依赖异味.xlsx"
            List<Node> cdSmellInfo = CyclicDependencyDetector.detect(orignGraph);
            CyclicDependencyExcelWriter.write(cdSmellInfo, prefix + "环依赖异味信息.xlsx");
            System.out.println("环依赖异味个数: " + cdSmellInfo.size());
            smellNum += cdSmellInfo.size();
            smellFileNum += findSubFiles(orignGraph, new HashSet<>(cdSmellInfo));
            smellComponentNodes.addAll(cdSmellInfo);
        }
        System.out.println("异味个数: " + smellNum);
        System.out.println("异味组件个数: " + smellComponentNodes.size());
        System.out.println("异味文件个数: " + smellFileNum);



        // 3、设置重构操作列表生成方案
        RefactorListGenerator refactorListGenerator = null; // 重构操作列表生成器
        switch (refactorListGeneratorNum) {
            case 1:
                refactorListGenerator = new RefactorListGenerator1(orignGraph, new ArrayList<>(smellComponentNodes));
                break;
            case 2:
                refactorListGenerator = new RefactorListGenerator2(orignGraph, new ArrayList<>(smellComponentNodes));
                break;
            default:
                System.out.println("无效重构操作列表生成方案, 请选择正确的重构操作列表生成方案");
                return;
        }


        ConsoleConstant.printTitle("耦合&&内聚指标");
        // 设置目标函数
        List<AbstractObjectiveFunction> objectiveFunctions = new ArrayList<>();
        // 添加异味消除率优化目标
        objectiveFunctions.add(new SmellEliminationRate(smellNum, refactorHL, refactorUD, refactorCD));
        // 添加耦合指标优化目标
        if (optimizedCoupling) {
            objectiveFunctions.add(new Coupling(orignGraph.filterAllComponentDependEdges().size()));
            System.out.println("耦合指标: " + orignGraph.filterAllComponentDependEdges().size());
        }
        // 添加内聚指标优化目标
        if (optimizedCohesion) {
            objectiveFunctions.add(new Cohesion());
            System.out.println("内聚指标: " + ObjectiveUtil.calCohesion(orignGraph));
        }
        if (!(optimizedCoupling || optimizedCohesion)) { // 此时认为为单目标优化算法
            objectiveFunctions.add(new Placeholder());
        }
        Configuration configuration = new Configuration(objectiveFunctions);

        /// 遗传算法
        // 设置种群生成器
        PopulationProducer populationProducer = new RefactorPopulationProducer(orignGraph); // 初始种群生成器
        GeneticCodeProducer geneticCodeProducer = new RefactorProducer(refactorListGenerator); // 基因型生成器
        ChildPopulationProducer childPopulationProducer = new RefactorChildPopulationProducer(orignGraph); // 子代生成器
        configuration.setPopulationProducer(populationProducer);
        configuration.setGeneticCodeProducer(geneticCodeProducer);
        configuration.setChildPopulationProducer(childPopulationProducer);

        // 设置参数
        configuration.setPopulationSize(populationSize); // 种群大小
        configuration.setGenerations(generationCount); // 演进代数
        configuration.setChromosomeLength(chromosomeLength); // 基因型长度

        // 设置算子
        CrossoverParticipantCreator selector = new ChampionshipSelector(); // 选择算子: 锦标赛选择策略
        AbstractCrossover crossover = new SingleCrossover(selector, crossoverProbability); // 交叉算子：单点交叉算子
        AbstractMutation mutation = new SingleMutation(mutationProbability, refactorListGenerator); // 变异算子：单点变异
        configuration.setCrossover(crossover);
        configuration.setMutation(mutation);

        // 设置终止条件
        TerminatingCriterion terminatingCriterion = new MyTermination(); // 终止条件：迭代到最大代数时终止
        configuration.setTerminatingCriterion(terminatingCriterion);

        // 设置输出实验过程数据图片目录
        configuration.setChartOutputCatalog(prefix);
        ConsoleConstant.printTitle("NSGA2算法参数");
        FileUtil.write2File(prefix + "NSGA2算法参数.txt", configuration.toString());
        System.out.println(configuration.toString()); // 输出配置信息

        // 运行NSGA2多目标优化算法
        top.lazyr.genetic.nsgaii.NSGA2 nsga2 = new top.lazyr.genetic.nsgaii.NSGA2(configuration);
        Population paretoOptimalPopulation = nsga2.run();

        // 输出pareto最优解到 exp2/重构建议[i].txt 和 exp2/重构后源代码模型图[i].xlsx
        List<Chromosome> paretoOptimalSolutions = paretoOptimalPopulation.getPopulace();
        for (int i = 0; i < paretoOptimalSolutions.size(); i++) {
            RefactorChromosome paretoOptimalSolution = (RefactorChromosome) paretoOptimalSolutions.get(i);
            SolutionWriter.write(paretoOptimalSolution, i, prefix);
        }
        System.out.println("pareto最优解写入成功...");

        // 展示pareto最优解
        List<Graph> refactoredGraphs = new ArrayList<>();
        List<List<String>> refactorsList = new ArrayList<>();
        for (int i = 0; i < paretoOptimalSolutions.size(); i++) {
            RefactorChromosome paretoOptimalSolution = (RefactorChromosome) paretoOptimalSolutions.get(i);
            refactoredGraphs.add(paretoOptimalSolution.getPhenotype());
            refactorsList.add(paretoOptimalSolution.getRefactors());
        }
        new SolutionFrame(orignGraph, refactoredGraphs, refactorsList);
    }

    /**
     * 获取所有componentNodes中的文件Node个数
     * @param componentNodes
     * @return
     */
    private static int findSubFiles(Graph graph, Set<Node> componentNodes) {
        int subFileNum  = 0;
        for (Node componentNode : componentNodes) {
            List<Node> subFileNodes = GraphManager.findSubFileNodes(graph, componentNode);
            subFileNum += subFileNodes == null ? 0 : subFileNodes.size();
        }
        return subFileNum;
    }


}
