package top.lazyr.refactor.list;

import top.lazyr.model.component.Graph;

import java.util.List;

/**
 * @author lazyr
 * @created 2022/1/28
 */
public interface RefactorListGenerator {

    /**
     * 随机生成一个重构操作
     * @return
     */
    String generateOne(int r);

    /**
     * 返回生成方案简介
     * @return
     */
    String getIntroduction();

}
