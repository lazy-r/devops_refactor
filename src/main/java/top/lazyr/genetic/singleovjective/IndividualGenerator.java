package top.lazyr.genetic.singleovjective;


import top.lazyr.genetic.singleovjective.fitness.Fitness;
import top.lazyr.genetic.singleovjective.model.Individual;
import top.lazyr.model.component.Graph;
import top.lazyr.model.manager.GraphManager;
import top.lazyr.refactor.action.RefactorActuator;

import java.util.List;

/**
 * @author lazyr
 * @created 2022/1/8
 */
public class IndividualGenerator {
    private Graph graph;
    private Fitness fitness;

    public IndividualGenerator(Graph graph, Fitness fitness) {
        this.graph = graph;
        this.fitness = fitness;
    }

    /**
     * 根据基因型生成个体
     * @param genotype
     * @return
     */
    public Individual generate(List<String> genotype) {
        Graph cloneGraph = GraphManager.cloneGraph(this.graph);
        int[] succeedFailed = RefactorActuator.refactor(cloneGraph, genotype);
        double score = fitness.cal(cloneGraph);
        Individual individual = Individual.builder()
                .genotype(genotype)
                .phenotype(cloneGraph)
                .score(score)
                .succeedNum(succeedFailed[0])
                .failedNum(succeedFailed[1])
                .build();
        return individual;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setFitness(Fitness fitness) {
        this.fitness = fitness;
    }
}
