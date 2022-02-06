package top.lazyr.genetic.nsgaii.objectivefunction;

import top.lazyr.genetic.nsgaii.model.chromosome.Chromosome;
import top.lazyr.genetic.nsgaii.model.chromosome.RefactorChromosome;
import top.lazyr.genetic.nsgaii.plugin.fitnesscalculator.FitnessCalculator;
import top.lazyr.model.component.Edge;
import top.lazyr.model.component.Graph;

import java.util.List;

/**
 * @author lazyr
 * @created 2022/1/29
 */
public class Coupling extends AbstractObjectiveFunction {
    private int originDependNum;

    public Coupling(int originDependNum) {
        this.originDependNum = originDependNum;
        this.objectiveFunctionTitle = "耦合";
    }


    public Coupling(FitnessCalculator fitnessCalculator) {
        super(fitnessCalculator);
    }

    /**
     * 耦合度：组件之间的依赖关系之和
     * 由于多目标优化是求最大值，而耦合度是越小越好，则以 初始耦合度 / 当前耦合度 为优化目标
     * @param chromosome
     * @return
     */
    @Override
    public double getValue(Chromosome chromosome) {
        RefactorChromosome myChromosome = (RefactorChromosome) chromosome;
        Graph graph = myChromosome.getPhenotype();
        List<Edge> dependEdges = graph.filterAllComponentDependEdges();
        // 若优化后无组件间的依赖关系，则值直接计为初始耦合度的两倍
        return dependEdges == null ? originDependNum * 2 : originDependNum / (double)dependEdges.size();
    }
}
