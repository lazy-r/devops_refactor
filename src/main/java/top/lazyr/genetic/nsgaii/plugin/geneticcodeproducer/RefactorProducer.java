package top.lazyr.genetic.nsgaii.plugin.geneticcodeproducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lazyr.constant.RefactorConstant;
import top.lazyr.genetic.nsgaii.model.allele.AbstractAllele;
import top.lazyr.genetic.nsgaii.model.allele.RefactorAllele;
import top.lazyr.refactor.list.RefactorListGenerator;

import java.util.*;

/**
 * @author lazyr
 * @created 2022/1/29
 */
public class RefactorProducer implements GeneticCodeProducer {
    private static Logger logger = LoggerFactory.getLogger(RefactorProducer.class);
    /* 用于生成重构操作的生成列表 */
    private RefactorListGenerator refactorListGenerator;

    public RefactorProducer(RefactorListGenerator refactorListGenerator) {
        this.refactorListGenerator = refactorListGenerator;
    }

    @Override
    public List<? extends AbstractAllele> produce(int length) {
        List<AbstractAllele> abstractAlleles = new ArrayList<>();
        List<String> refactors = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            String refactor = refactorListGenerator.generateOne(length);
            while (!refactorValidate(refactors, refactor)) { // 若无效，则继续随机生成，直到有效为止
                refactor = refactorListGenerator.generateOne(length);

            }
            refactors.add(refactor);
        }

        for (int i = 0; i < length; i++) {
            abstractAlleles.add(new RefactorAllele(refactors.get(i)));
        }
        return abstractAlleles;
    }


    /**
     * 若refactor中重构的文件和refactors中要重构的文件已经重复，则认为refactor无效，返回false;
     * 否则返回true
     * @param refactors
     * @param refactor
     * @return
     */
    private boolean refactorValidate(List<String> refactors, String refactor) {
        if (refactors == null) {
            return false;
        }
        if (refactors.size() == 0) {
            return true;
        }

        // 已经被重构的文件
        Set<String> existedRefactoredFileIds = new HashSet<>();
        for (String r : refactors) {
            Set<String> fileIds = getFileIdsFromRefactor(r);
            existedRefactoredFileIds.addAll(fileIds);
        }

        Set<String> fileIds = getFileIdsFromRefactor(refactor);

        boolean existed = false;
        for (String fileId : fileIds) {
            if (existedRefactoredFileIds.contains(fileId)) {
                existed = true;
            }
            existedRefactoredFileIds.add(fileId);
        }
        return !existed;
    }

    /**
     * 从重构格式字符串中抽取待重构的文件列表
     * @param refactor
     * @return
     */
    private Set<String> getFileIdsFromRefactor(String refactor) {
        String[] actions = refactor.split(RefactorConstant.ACTION_SEPARATOR);
        Set<String> fileIds = new HashSet<>();
        switch (actions[0]) {
            case RefactorConstant.MOVE_FILE:
                fileIds.add(actions[2]);
                break;
            case RefactorConstant.EXTRACT_COMPONENT:
                String[] componentFileIds = actions[1].split(RefactorConstant.EXTRACT_COMPONENT_SEPARATOR);
                for (String componentFileId : componentFileIds) {
                    fileIds.add(componentFileId.split(RefactorConstant.BELONG_SEPARATOR)[1]);
                }
                break;
        }
        return fileIds;
    }

    @Override
    public String toString() {
        return refactorListGenerator.getIntroduction();
    }
}
