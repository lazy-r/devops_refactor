package top.lazyr;

import top.lazyr.genetic.chart.soultionframe.SolutionPanel;
import top.lazyr.genetic.singleovjective.GeneticRule;
import top.lazyr.genetic.singleovjective.IndividualGenerator;
import top.lazyr.genetic.singleovjective.Nature;
import top.lazyr.genetic.singleovjective.crossover.Crossover;
import top.lazyr.genetic.singleovjective.crossover.SinglePointCrossover;
import top.lazyr.genetic.singleovjective.fitness.Fitness;
import top.lazyr.genetic.singleovjective.fitness.Fitness1;
import top.lazyr.genetic.singleovjective.model.Individual;
import top.lazyr.genetic.singleovjective.mutation.Mutation;
import top.lazyr.genetic.singleovjective.mutation.SinglePointMutation;
import top.lazyr.genetic.singleovjective.select.RouletteWheelsSelector;
import top.lazyr.genetic.singleovjective.select.Selector;
import top.lazyr.genetic.singleovjective.writer.SolutionWriter;
import top.lazyr.model.component.Graph;
import top.lazyr.model.component.Node;
import top.lazyr.model.parser.Parser;
import top.lazyr.model.parser.SourceCodeParser;
import top.lazyr.model.writer.ExcelModelWriter;
import top.lazyr.refactor.list.RefactorListGenerator;
import top.lazyr.refactor.list.RefactorListGenerator1;
import top.lazyr.smell.detector.hublikedependency.HubLikeDependencyDetector;
import top.lazyr.smell.detector.hublikedependency.HubLikeDependencyExcelWriter;
import top.lazyr.util.ArrayUtil;
import top.lazyr.util.FileUtil;

import java.util.*;

/**
 * @author lazyr
 * @created 2022/1/31
 */
public class SingleObjectiveTest {
    /* 种群大小 */
    private static int N = 40;
    /* 解的大小 */
    private static int R = 40;
    /* 交叉概率, 0 <= Pc <= 100 */
    private static int Pc = 80;
    /* 变异概率, 0 <= Pm <= 100 */
    private static int Pm = 60;
    /* 演进代数 */
    private static int T = 100;
    /* 解的个数 */
    private static int M = 10;
    /* 重构操作列表生成方式: 1表示使用RefactorList1生成 */
    private static int refactorListNum = 1;
    /* 输出目录前缀 */
    private static String prefix = "exp1/";



    public static void main(String[] args) {
        // 重构项目绝对路径
        String[] paths = new String[]{
//                "/Users/lazyr/Work/projects/devops/test/data/refactor_demo/target/classes",
//                "/Users/lazyr/Work/graduation/arcan/data/jedit/org",
//                "/Users/lazyr/Work/graduation/arcan/data/ant/org",
                "/Users/lazyr/Work/graduation/arcan/data/pig/org",
//                "/Users/lazyr/Work/graduation/arcan/data/hutool/cn",
//                "/Users/lazyr/Work/graduation/arcan/data/eclipse/org",
//                "/Users/lazyr/Work/graduation/arcan/data/lucene-core/org",
//                "/Users/lazyr/Work/graduation/arcan/data/spark-core/org",
//                "/Users/lazyr/Work/graduation/arcan/data/hadoop-common/org",
//                "/Users/lazyr/Work/graduation/arcan/data/dubbo/com",
        };
        for (String path : paths) {
            refactor(path);
        }
    }


    public static void refactor(String path) {
        Parser parser = new SourceCodeParser();
        // 1、源码解析器，生成源代码模型图
        Graph graph = parser.parse(path);
        new ExcelModelWriter().write(graph);

        // 2、异味检测器，获取异味Node
        // 异味检测
        Map<Node, List<Integer>> hlSmellInfo = HubLikeDependencyDetector.detect(graph);
        // 输出枢纽型异味信息到 "exp1/枢纽型依赖异味.xlsx"
        HubLikeDependencyExcelWriter.write(hlSmellInfo, prefix + "枢纽型依赖异味.xlsx");

        // 统计异味信息
        int smellNum = hlSmellInfo.size();
        System.out.println("初始枢纽型异味个数: " + smellNum);

        // 记录异味组件个数
        Set<Node> smellComponentNodes = new HashSet<>();
        for (Node node : hlSmellInfo.keySet()) {
            smellComponentNodes.add(node);
        }


        // 3、遗传算法
        // 重构操作执行器
        StringBuilder params = new StringBuilder();
        params.append("N = " + N + "\n");
        params.append("R = " + R + "\n");
        params.append("Pc = " + Pc + "\n");
        params.append("Pm = " + Pm + "\n");
        params.append("T = " + T + "\n");
        params.append("M = " + M);
        FileUtil.write2File(prefix + "params.txt", params.toString());


        // 重构操作列表生成器
        RefactorListGenerator refactorList = new RefactorListGenerator1(graph, ArrayUtil.set2List(smellComponentNodes));
        // 适应度函数
        Fitness fitness = new Fitness1(smellComponentNodes.size());

        // 个体生成器：重构操作执行器、重构操作列表生成器、适应度函数
        IndividualGenerator generator = new IndividualGenerator(graph, fitness);


        // 选择操作
        Selector rouletteWheelsSelector = new RouletteWheelsSelector();
        // 交叉操作：交叉概率
        Crossover singlePointCrossover = new SinglePointCrossover(Pc);
        // 变异操作：重构规则生成器、变异概率
        Mutation mutation = new SinglePointMutation(refactorList, Pm);

        // 遗传规则：选择操作、交叉操作、编译操作、个体生成器、种群大小
        GeneticRule geneticRule = new GeneticRule(
                rouletteWheelsSelector,
                singlePointCrossover,
                mutation,
                generator, N);


        // 自然界：种群大小、基因型大小、迭代次数、解的个数、遗传规则、重构规则生成器、个体生成器
        Nature nature = new Nature(N, R, T, M, geneticRule, refactorList, generator);

        List<Individual> bestIndividuals = nature.optimal1();
        Individual individual = bestIndividuals.get(0);
        System.out.println("最优值 => " + individual.getScore());
        new SolutionWriter().write(individual);
        new SolutionPanel(graph, individual.getPhenotype(), individual.getGenotype());
    }
}
