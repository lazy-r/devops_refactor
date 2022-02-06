package top.lazyr.genetic.nsgaii.objectivefunction;

import top.lazyr.genetic.nsgaii.Service;
import top.lazyr.genetic.nsgaii.model.chromosome.Chromosome;
import top.lazyr.genetic.nsgaii.plugin.fitnesscalculator.FitnessCalculator;

public class SCH_1 extends AbstractObjectiveFunction {

	public SCH_1(FitnessCalculator fitnessCalculator) {
		super(fitnessCalculator);
		this.objectiveFunctionTitle = "pow(x, 2)";
	}

	@Override
	public double getValue(Chromosome chromosome) {
		return Service.roundOff(
			Math.pow(this.fitnessCalculator.calculate(chromosome), 2),
			4
		);
	}
}
