package top.lazyr.genetic.nsgaii.writer;

import top.lazyr.genetic.nsgaii.model.chromosome.Chromosome;
import top.lazyr.genetic.nsgaii.model.chromosome.RefactorChromosome;
import top.lazyr.model.component.Graph;
import top.lazyr.model.writer.ExcelModelWriter;
import top.lazyr.util.FileUtil;

import java.util.List;

/**
 * @author lazyr
 * @created 2022/2/6
 */
public class SolutionWriter {
    /**
     *
     * @param chromosome
     * @param num
     */
    public static void write(RefactorChromosome chromosome, int num, String outputPath) {
        Graph phenotype = chromosome.getPhenotype();
        List<String> refactors = chromosome.getRefactors();
        FileUtil.write2File(outputPath + "重构建议" + num + ".txt",
                            refactors.toString().replace("[", "")
                            .replace("]", "")
                            .replace(", ", "\n"));
        ExcelModelWriter.write(phenotype, outputPath + "重构后源代码模型图" + num + ".xlsx");
    }
}
