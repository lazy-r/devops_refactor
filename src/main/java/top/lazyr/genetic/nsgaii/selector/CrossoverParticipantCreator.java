package top.lazyr.genetic.nsgaii.selector;

import top.lazyr.genetic.nsgaii.model.chromosome.Chromosome;
import top.lazyr.genetic.nsgaii.model.population.Population;

import java.util.List;

@FunctionalInterface
public interface CrossoverParticipantCreator {
	List<Chromosome> create(Population population);
}
