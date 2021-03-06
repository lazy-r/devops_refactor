package top.lazyr.genetic.chart;

import org.junit.Test;
import top.lazyr.constant.ConsoleConstant;
import top.lazyr.genetic.chart.soultionframe.SolutionPanel;
import top.lazyr.genetic.nsgaii.Configuration;
import top.lazyr.genetic.nsgaii.NSGA2;
import top.lazyr.genetic.nsgaii.crossover.AbstractCrossover;
import top.lazyr.genetic.nsgaii.crossover.SingleCrossover;
import top.lazyr.genetic.nsgaii.model.allele.AbstractAllele;
import top.lazyr.genetic.nsgaii.model.chromosome.Chromosome;
import top.lazyr.genetic.nsgaii.model.chromosome.RefactorChromosome;
import top.lazyr.genetic.nsgaii.model.population.Population;
import top.lazyr.genetic.nsgaii.mutation.AbstractMutation;
import top.lazyr.genetic.nsgaii.mutation.SingleMutation;
import top.lazyr.genetic.nsgaii.objectivefunction.AbstractObjectiveFunction;
import top.lazyr.genetic.nsgaii.objectivefunction.Coupling;
import top.lazyr.genetic.nsgaii.objectivefunction.SmellEliminationRate;
import top.lazyr.genetic.nsgaii.plugin.childpopulationproducer.ChildPopulationProducer;
import top.lazyr.genetic.nsgaii.plugin.childpopulationproducer.RefactorChildPopulationProducer;
import top.lazyr.genetic.nsgaii.plugin.geneticcodeproducer.GeneticCodeProducer;
import top.lazyr.genetic.nsgaii.plugin.geneticcodeproducer.RefactorProducer;
import top.lazyr.genetic.nsgaii.plugin.populationproducer.PopulationProducer;
import top.lazyr.genetic.nsgaii.plugin.populationproducer.RefactorPopulationProducer;
import top.lazyr.genetic.nsgaii.selector.ChampionshipSelector;
import top.lazyr.genetic.nsgaii.selector.CrossoverParticipantCreator;
import top.lazyr.genetic.nsgaii.termination.MyTermination;
import top.lazyr.genetic.nsgaii.termination.TerminatingCriterion;
import top.lazyr.model.component.Graph;
import top.lazyr.model.component.Node;
import top.lazyr.model.parser.Parser;
import top.lazyr.model.parser.SourceCodeParser;
import top.lazyr.model.writer.ExcelModelWriter;
import top.lazyr.refactor.list.RefactorListGenerator;
import top.lazyr.refactor.list.RefactorListGenerator1;
import top.lazyr.smell.detector.cyclicdependency.CyclicDependencyDetector;
import top.lazyr.smell.detector.cyclicdependency.CyclicDependencyExcelWriter;
import top.lazyr.smell.detector.hublikedependency.HubLikeDependencyDetector;
import top.lazyr.smell.detector.hublikedependency.HubLikeDependencyExcelWriter;
import top.lazyr.smell.detector.unstabledependency.UnstableDependencyDetector;
import top.lazyr.smell.detector.unstabledependency.UnstableDependencyExcelWriter;
import top.lazyr.util.ArrayUtil;

import java.util.*;

public class FileChartTest {
    int populationSize = 4;
    int generationCount = 2;
    int chromosomeLength = 4;
    float mutationProbability = 0.6f;
    float crossoverProbability = 0.8f;

    @Test
    public void refactor() {
        String path = "/Users/lazyr/Work/projects/devops/test/data/refactor_demo/target/classes";
        // ???????????????????????????????????? "resources/exp/??????????????????.xlsx"
        Graph graph = parse(path);



        // ????????????
        Map<Node, List<Node>> udSmellInfo = UnstableDependencyDetector.detect(graph);
        Map<Node, List<Integer>> hlSmellInfo = HubLikeDependencyDetector.detect(graph);
        List<Node> cdSmellInfo = CyclicDependencyDetector.detect(graph);
        // ???????????????????????????????????? "resources/exp/?????????????????????.xlsx"
        UnstableDependencyExcelWriter.write(udSmellInfo, "?????????????????????.xlsx");
        // ?????????????????????????????? "resources/exp/?????????????????????.xlsx"
        HubLikeDependencyExcelWriter.write(hlSmellInfo, "?????????????????????.xlsx");
        // ?????????????????????????????? "resources/exp/???????????????.xlsx"
        CyclicDependencyExcelWriter.write(cdSmellInfo, "???????????????.xlsx");

        // ??????????????????
        int smellNum = udSmellInfo.size() + hlSmellInfo.size() + cdSmellInfo.size();
        System.out.println("??????????????????: " + smellNum);

        // ????????????????????????
        Set<Node> smellComponentNodes = new HashSet<>();
        for (Node node : udSmellInfo.keySet()) {
            smellComponentNodes.add(node);
        }
        for (Node node : hlSmellInfo.keySet()) {
            smellComponentNodes.add(node);
        }
        for (Node node : cdSmellInfo) {
            smellComponentNodes.add(node);
        }

        // ??????????????????
        List<AbstractObjectiveFunction> objectiveFunctions = new ArrayList<>();
        objectiveFunctions.add(new SmellEliminationRate(smellNum, true, true, true));
        objectiveFunctions.add(new Coupling(graph.filterAllFileDependEdges().size()));
//        objectiveFunctions.add(new Cohesion());

        Configuration configuration = new Configuration(objectiveFunctions);

        /// ????????????
        // ?????????????????????
        RefactorListGenerator refactorList = new RefactorListGenerator1(graph, ArrayUtil.set2List(smellComponentNodes));
        PopulationProducer populationProducer = new RefactorPopulationProducer(graph);
        GeneticCodeProducer geneticCodeProducer = new RefactorProducer(refactorList);
        ChildPopulationProducer childPopulationProducer = new RefactorChildPopulationProducer(graph);
        configuration.setPopulationProducer(populationProducer);
        configuration.setGeneticCodeProducer(geneticCodeProducer);
        configuration.setChildPopulationProducer(childPopulationProducer);

        // ????????????
        configuration.setPopulationSize(populationSize);
        configuration.setGenerations(generationCount);
        configuration.setChromosomeLength(chromosomeLength);

        // ????????????
        CrossoverParticipantCreator selector = new ChampionshipSelector();
        AbstractMutation mutation = new SingleMutation(mutationProbability, refactorList);
        AbstractCrossover crossover = new SingleCrossover(selector, crossoverProbability);
        configuration.setCrossover(crossover);
        configuration.setMutation(mutation);

        // ??????????????????
        TerminatingCriterion terminatingCriterion = new MyTermination();
        configuration.setTerminatingCriterion(terminatingCriterion);

        System.out.println(configuration.toString());
        NSGA2 nsga2 = new NSGA2(configuration);
        Population population = nsga2.run();
        List<Chromosome> populace = population.getPopulace();
        Chromosome chromosome1 = populace.get(0);
        ConsoleConstant.printTitle("?????????");
        List<AbstractAllele> geneticCode = chromosome1.getGeneticCode();
        for (AbstractAllele abstractAllele : geneticCode) {
            System.out.println(abstractAllele);
        }
        Graph phenotype = ((RefactorChromosome) chromosome1).getPhenotype();
        new SolutionPanel(graph, phenotype, ((RefactorChromosome) chromosome1).getRefactors());


    }

    public Graph parse(String path) {
        // ????????????????????????
        Parser parser = new SourceCodeParser();
        Graph graph = parser.parse(path);
        // ????????????????????????????????? "resources/exp/??????????????????.xlsx"
        ExcelModelWriter.write(graph);
        return graph;
    }
}
