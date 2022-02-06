package top.lazyr.refactor.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lazyr.constant.RefactorConstant;
import top.lazyr.model.component.Graph;
import top.lazyr.refactor.atom.Actuator;
import top.lazyr.util.ArrayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lazyr
 * @created 2022/1/28
 */
public class RefactorActuator {
    private static Logger logger = LoggerFactory.getLogger(RefactorActuator.class);


    /**
     * moveClasses,classA;classB;classC,packageName
     * extractPackage,classA;classB;classC,from_PackageName
     * 根据refactor语句对graph进行重构
     * - 返回refactor语句中对要重构文件的行重构操作成功的个数和操作失败的个数，[succeedNum, failedNum]
     * @param graph
     * @param refactor
     */
    public static int[] refactor(Graph graph, String refactor) {
        int[] succeedFailed = new int[2];
        if (!refactorValidator(refactor)) {
            logger.error("refactor string({}) is wrong.", refactor);
            return new int[]{0, 0};
        }

        String[] actions = refactor.split(RefactorConstant.ACTION_SEPARATOR);
        switch (actions[0]) {
            case RefactorConstant.MOVE_FILE:
                succeedFailed = Actuator.moveFile(graph, refactor);
                break;
            case RefactorConstant.EXTRACT_COMPONENT:
                succeedFailed = Actuator.extractComponent(graph, refactor);
                break;
        }
        return succeedFailed;
    }

    /**
     * 根据一组refactors语句按顺序对graph进行重构
     * - 返回一组refactors语句中对要重构类的行重构操作成功的个数和操作失败的个数之和，[succeedNum, failedNum]
     * 返回对重构文件的执行重构操作成功的个数和操作失败的个数
     * [succeedNum, failedNum]
     * @param graph
     * @param refactors
     */
    public static int[] refactor(Graph graph, List<String> refactors) {
        int succeedNum = 0;
        int failedNum = 0;
        if (graph == null || refactors == null) {
            logger.warn("the params is empty.");
            return new int[]{0, 0};
        }
        for (String refactor : refactors) {
            int[] tempSucceedFailed = refactor(graph, refactor);
            succeedNum += tempSucceedFailed[0];
            failedNum += tempSucceedFailed[1];
        }

        return new int[]{succeedNum, failedNum};
    }

    /**
     * 验证重构操作字符串是否符合格式，合法重构字符串字符串
     * - moveFile,sourceComponentNodeId,fileNodeId,targetComponentNodeId
     * - extractComponent,sourceComponentNodeId,fileNodeId1;fileNodeId2;fileNodeId3,from_sourceComponentNodeId
     * @param refactor
     * @return
     */
    private static boolean refactorValidator(String refactor) {
        String[] actions = refactor.split(RefactorConstant.ACTION_SEPARATOR);
        return  actions.length == 4 || actions.length == 3;
    }
}
