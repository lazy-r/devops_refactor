package top.lazyr.constant;

/**
 * @author lazyr
 * @created 2022/1/6
 */
public interface RefactorConstant {
    /**
     * 移动一个文件到一个项目内的组件中
     */
    String MOVE_FILE = "moveFile";
    /**
     * 抽取一组文件到一个新的组件中
     */
    String EXTRACT_COMPONENT = "extractComponent";
    /**
     * 操作分割符
     */
    String ACTION_SEPARATOR = ",";
    /**
     * EXTRACT_COMPONENT操作专用分隔符
     * extractComponent,sourceComponentNodeId1>fileNodeId1;sourceComponentNodeId2>fileNodeId2,from_sourceComponentNodeId
     * 表示
     * 将 sourceComponentNodeId1组件内的fileNodeId1 和 sourceComponentNodeId2组件下的fileNodeId2 抽取到from_sourceComponentNodeId组件中
     */
    String EXTRACT_COMPONENT_SEPARATOR = ";";
    String BELONG_SEPARATOR = ">";

}
