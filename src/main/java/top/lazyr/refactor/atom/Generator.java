package top.lazyr.refactor.atom;

import top.lazyr.constant.RefactorConstant;

import java.util.Map;

/**
 * @author lazyr
 * @created 2022/1/28
 */
public class Generator {

    /**
     * 生成moveFile格式原子操作字符串
     * 生成格式: moveFile,sourceComponentNodeId,fileNodeId,targetComponentNodeId
     * - 若fileNodeId为""，则返回""
     * @param sourceComponentNodeId
     * @param fileNodeId
     * @param targetComponentNodeId
     * @return
     */
    public static String moveFile(String sourceComponentNodeId, String fileNodeId, String targetComponentNodeId) {
        if (fileNodeId.equals("")) {
            return "";
        }
        StringBuilder moveFile = new StringBuilder();
        moveFile.append(RefactorConstant.MOVE_FILE +
                            RefactorConstant.ACTION_SEPARATOR +
                            sourceComponentNodeId +
                            RefactorConstant.ACTION_SEPARATOR +
                            fileNodeId +
                            RefactorConstant.ACTION_SEPARATOR +
                            targetComponentNodeId);
        return moveFile.toString();
    }

    /**
     * 生成extractComponent格式原子操作字符串
     * 生成格式: extractComponent,sourceComponentNodeId1>fileNodeId1;sourceComponentNodeId2>fileNodeId2,from_sourceComponentNodeId
     * - 若fileNames为空，则返回""
     * - 对同一个sourceComponentNodeId每次抽取到的新组件名都是from_sourceComponentNodeId
     * @param sourceComponentNodeId
     * @param fileComponentIds
     * @return
     */
    public static String extractComponent(String sourceComponentNodeId, Map<String, String> fileComponentIds) {
        if (fileComponentIds == null || fileComponentIds.size() == 0) {
            return "";
        }
        StringBuilder extractComponent = new StringBuilder();
        extractComponent.append(RefactorConstant.EXTRACT_COMPONENT +
                                    RefactorConstant.ACTION_SEPARATOR);

        int i = 0;
        for (String componentId : fileComponentIds.keySet()) {
            extractComponent.append(componentId + RefactorConstant.BELONG_SEPARATOR + fileComponentIds.get(componentId));
            if (i++ != fileComponentIds.size() - 1) {
                extractComponent.append(RefactorConstant.EXTRACT_COMPONENT_SEPARATOR);
            }
        }

        extractComponent.append(RefactorConstant.ACTION_SEPARATOR + newComponentNodeId(sourceComponentNodeId));
        return extractComponent.toString();
    }

    /**
     * a.b.c; [level]; [from] => a.b.from_c; [level]; [from]
     * @param packageName
     * @return
     */
    private static String newComponentNodeId(String packageName) {
        // TODO:考虑packageName不合法的情况，如packageName=""
        if (!packageName.contains(".")) {
            return "from_" + packageName;
        }
        String prefix = packageName.substring(0, packageName.lastIndexOf("."));
        String currentPackageName = packageName.substring(packageName.lastIndexOf(".") + 1);
        return prefix + ".from_" + currentPackageName;
    }
}
