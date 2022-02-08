package top.lazyr.refactor.list;

import top.lazyr.constant.RefactorConstant;
import top.lazyr.model.component.Graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lazyr
 * @created 2022/1/28
 */
public abstract class RefactorListGenerator {

    /**
     * 随机生成一个重构操作
     * @return
     */
    public abstract String generateOne(int r);

    /**
     * 在refactors之后随机生成一个重构操作
     * @return
     */
    public abstract String generateOne(List<String> refactors, int r);

    /**
     * 生成r个互不冲突的重构操作
     * @param r
     * @return
     */
    public abstract List<String> generateList(int r);

    /**
     * 返回生成方案简介
     * @return
     */
    public abstract String getIntroduction();

    /**
     * 若refactor中重构的文件和refactors中要重构的文件已经重复，则认为refactor无效，返回false;
     * 否则返回true
     * @param refactors
     * @param refactor
     * @return
     */
    protected boolean refactorValidate(List<String> refactors, String refactor) {
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

}
