package top.lazyr.genetic.nsgaii.model.chromosome;

import top.lazyr.genetic.nsgaii.model.allele.AbstractAllele;
import top.lazyr.model.component.Graph;
import top.lazyr.model.manager.GraphManager;
import top.lazyr.refactor.action.RefactorActuator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lazyr
 * @created 2022/1/29
 */
public class RefactorChromosome extends Chromosome {
    /* 初始graph，用于克隆时还传递初始graph */
    private Graph initGraph;
    /* 表现型：克隆graph后根据基因型进行重构后 */
    private Graph refactorGraph;
    private int succeed;
    private int failed;

    public RefactorChromosome(List<? extends AbstractAllele> geneticCode, Graph graph) {
        super(geneticCode);
        this.initGraph = graph;
    }

    public RefactorChromosome(RefactorChromosome chromosome) {
        super(chromosome);
        this.initGraph = chromosome.initGraph;
    }

    public Graph getPhenotype() {
        if (refactorGraph == null) {
            refactor();
        }
        return refactorGraph;
    }

    public Graph getInitGraph() {
        return initGraph;
    }

    /**
     * 深度克隆initGraph后，进行重构生成表现型
     */
    private void refactor() {
        List<String> refactors = getRefactors();
        this.refactorGraph = GraphManager.cloneGraph(initGraph);
        int[] succeedFailed = RefactorActuator.refactor(refactorGraph, refactors);
        succeed = succeedFailed[0];
        failed = succeedFailed[1];
    }

    public int getSucceed() {
        if (refactorGraph == null) {
            refactor();
        }
        return succeed;
    }

    public void setSucceed(int succeed) {
        if (refactorGraph == null) {
            refactor();
        }
        this.succeed = succeed;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public List<String> getRefactors() {
        List<AbstractAllele> geneticCode = getGeneticCode();
        List<String> refactors = new ArrayList<>();
        for (AbstractAllele abstractAllele : geneticCode) {
            refactors.add((String) abstractAllele.getGene());
        }
        return refactors;
    }

    @Override
    public Chromosome getCopy() {
        return new RefactorChromosome(this);
    }


}
