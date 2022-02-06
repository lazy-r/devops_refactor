package top.lazyr.genetic.singleovjective.writer;


import top.lazyr.genetic.singleovjective.model.Individual;

import java.util.List;

/**
 * @author lazyr
 * @created 2022/1/13
 */
public interface ResultWriter {
    void write(Individual individual);
    void write(List<Individual> individuals);
}
