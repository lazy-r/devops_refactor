package top.lazyr.genetic.singleovjective.fitness;


import top.lazyr.model.component.Graph;

/**
 * @author lazyr
 * @created 2022/1/7
 */
public interface Fitness {
    /**
     * 计算individual的适应度值
     * @param phenotype
     * @return
     */
    double cal(Graph phenotype);
}
