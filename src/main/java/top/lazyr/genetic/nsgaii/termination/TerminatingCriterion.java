package top.lazyr.genetic.nsgaii.termination;

import top.lazyr.genetic.nsgaii.model.population.Population;

@FunctionalInterface
public interface TerminatingCriterion {
	boolean shouldRun(Population population, int generationCount, int maxGenerations);
}
