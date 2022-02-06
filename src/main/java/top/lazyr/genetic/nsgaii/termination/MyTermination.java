package top.lazyr.genetic.nsgaii.termination;


import top.lazyr.genetic.nsgaii.model.population.Population;

/**
 * @author lazyr
 * @created 2022/1/29
 */
public class MyTermination implements TerminatingCriterion {

    @Override
    public boolean shouldRun(Population population, int generationCount, int maxGenerations) {
        return generationCount <= maxGenerations;
    }

    @Override
    public String toString() {
        return "迭代到最大代";
    }
}
