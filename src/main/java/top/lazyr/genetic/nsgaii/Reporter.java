package top.lazyr.genetic.nsgaii;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import top.lazyr.genetic.nsgaii.model.chromosome.Chromosome;
import top.lazyr.genetic.nsgaii.model.population.Population;
import top.lazyr.genetic.chart.LineChart;
import top.lazyr.genetic.nsgaii.objectivefunction.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Reporter {
	private static List<LineChart> lineCharts;
	private static List<AbstractObjectiveFunction> objectiveFunctions;
	private static String prefix = "./src/main/resources/";
	public static String outputCatalog = "";

	public static void init(Configuration configuration) {
		objectiveFunctions = configuration.objectives;
		lineCharts = new ArrayList<>();
		for (AbstractObjectiveFunction objectiveFunction : objectiveFunctions) {
			lineCharts.add(new LineChart(objectiveFunction.getObjectiveTitle(), "代", objectiveFunction.getObjectiveTitle()));
		}
		for (LineChart lineChart : lineCharts) {
			lineChart.show();
		}
	}

	public static void reportGeneration(Population child, int generation) {
		List<Double> averChildObjectives = averObjectives(child);

		for (int i = 0; i < lineCharts.size(); i++) {
			lineCharts.get(i).addDate(objectiveFunctions.get(i).getObjectiveTitle(), generation + "", averChildObjectives.get(i));
		}


	}

	private static List<Double> averObjectives(Population population) {
		List<Chromosome> chromosomes = population.getPopulace();
		int size = chromosomes.size();
		List<Double> averObjectives = Arrays.asList(0d, 0d, 0d, 0d, 0d);
		for (Chromosome chromosome : chromosomes) {
			List<Double> objectiveValues = chromosome.getObjectiveValues();
			for (int i = 0; i < objectiveValues.size(); i++) {
				averObjectives.set(i, averObjectives.get(i) + objectiveValues.get(i));
			}
		}
		for (int i = 0; i < averObjectives.size(); i++) {
			averObjectives.set(i, averObjectives.get(i) / size);
		}
		return averObjectives;
	}

	public static void terminate() {
		for (int i = 0; i < lineCharts.size(); i++) {
			String objectiveTitle = objectiveFunctions.get(i).getObjectiveTitle();
			FileOutputStream out = null;
			try {
				File outFile = new File(prefix + outputCatalog + objectiveTitle + ".jpeg");
				if (!outFile.getParentFile().exists()) {
					outFile.getParentFile().mkdirs();
				}
				ChartUtils.saveChartAsJPEG(outFile, lineCharts.get(i).getJFreeChart(), 800, 500);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		System.out.println("实验过程数据图片写入成功...");
	}

}
