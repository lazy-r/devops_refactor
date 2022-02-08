package top.lazyr.genetic.nsgaii.solution;

import top.lazyr.constant.ConsoleConstant;
import top.lazyr.genetic.nsgaii.model.chromosome.Chromosome;
import top.lazyr.genetic.nsgaii.model.chromosome.RefactorChromosome;
import top.lazyr.model.component.Graph;
import top.lazyr.model.component.Node;
import top.lazyr.smell.detector.cyclicdependency.CyclicDependencyDetector;
import top.lazyr.smell.detector.hublikedependency.HubLikeDependencyDetector;
import top.lazyr.smell.detector.unstabledependency.UnstableDependencyDetector;
import top.lazyr.util.ObjectiveUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lazyr
 * @created 2022/2/8
 */
public class SolutionFilter {

    public static List<Chromosome> filter(List<Chromosome> paretoOptimalSolutions) {
        List<Chromosome> filterSolutions = new ArrayList<>();
        Graph originGraph = ((RefactorChromosome)paretoOptimalSolutions.get(0)).getOriginGraph();

        Set<Node> originHLNodes = HubLikeDependencyDetector.detect(originGraph).keySet();
        Set<Node> originUDNodes = UnstableDependencyDetector.detect(originGraph).keySet();
        Set<Node> originCDNodes = new HashSet<>(CyclicDependencyDetector.detect(originGraph));

        Set<Node> originSmellNodes = new HashSet<>();
        originSmellNodes.addAll(originHLNodes);
        originSmellNodes.addAll(originUDNodes);
        originSmellNodes.addAll(originCDNodes);
        int originTotalSmellNum = (originHLNodes.size() + originUDNodes.size() + originCDNodes.size());
        int originSmellComponentNum = originSmellNodes.size();
        double originCohesion= ObjectiveUtil.calCohesion(originGraph);
        double originCoupling = ObjectiveUtil.calCoupling(originGraph);
        for (int i = 0; i < paretoOptimalSolutions.size(); i++) {
            Graph refactoredGraph = ((RefactorChromosome)paretoOptimalSolutions.get(i)).getPhenotype();
            Set<Node> refactoredHLNodes = HubLikeDependencyDetector.detect(refactoredGraph).keySet();
            Set<Node> refactoredUDNodes = UnstableDependencyDetector.detect(refactoredGraph).keySet();
            Set<Node> refactoredCDNodes = new HashSet<>(CyclicDependencyDetector.detect(refactoredGraph));

            Set<Node> refactoredSmellNodes = new HashSet<>();
            refactoredSmellNodes.addAll(refactoredHLNodes);
            refactoredSmellNodes.addAll(refactoredUDNodes);
            refactoredSmellNodes.addAll(refactoredCDNodes);
            int refactoredTotalSmellNum = refactoredHLNodes.size() + refactoredUDNodes.size() + refactoredCDNodes.size();
            int refactoredSmellComponentNum = refactoredSmellNodes.size();
            double refactoredCohesion = ObjectiveUtil.calCohesion(refactoredGraph);
            double refactoredCoupling = ObjectiveUtil.calCoupling(refactoredGraph);
            if (refactoredCohesion >= originCohesion &&
                    refactoredCoupling <= originCoupling &&
                    refactoredTotalSmellNum <= originTotalSmellNum &&
                    refactoredSmellComponentNum <= originSmellComponentNum) {
                filterSolutions.add(paretoOptimalSolutions.get(i));
            }
        }
        return filterSolutions;
    }
}
