package top.lazyr.genetic.nsgaii.plugin.fitnesscalculator;


import top.lazyr.genetic.nsgaii.model.chromosome.Chromosome;

@FunctionalInterface
public interface FitnessCalculator {
	double calculate(Chromosome chromosome);
}
