package top.lazyr.genetic.nsgaii.plugin.childpopulationproducer;

import top.lazyr.genetic.nsgaii.Service;
import top.lazyr.genetic.nsgaii.crossover.AbstractCrossover;
import top.lazyr.genetic.nsgaii.model.chromosome.Chromosome;
import top.lazyr.genetic.nsgaii.model.population.Population;
import top.lazyr.genetic.nsgaii.mutation.AbstractMutation;
import top.lazyr.model.component.Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lazyr
 * @created 2022/1/29
 */
public class RefactorChildPopulationProducer implements ChildPopulationProducer {
    private Graph graph;

    public RefactorChildPopulationProducer(Graph graph) {
        this.graph = graph;
    }

    @Override
    public Population produce(Population parentPopulation, AbstractCrossover crossover, AbstractMutation mutation, int populationSize) {
        List<Chromosome> populace = new ArrayList<>();

        while(populace.size() < populationSize)
            if((populationSize - populace.size()) == 1)
                populace.add(
                        mutation.perform(
                                Service.crowdedBinaryTournamentSelection(parentPopulation).getCopy()
                        )
                );
            else
                for(Chromosome chromosome : crossover.perform(parentPopulation))
                    populace.add(mutation.perform(chromosome));

        
        return new Population(populace);
    }

    @Override
    public String toString() {
        return "常规子代生成器";
    }
}
