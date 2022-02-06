package top.lazyr.model.parser;

import top.lazyr.model.component.Graph;

/**
 * @author lazyr
 * @created 2022/1/25
 */
public interface Parser {
    Graph parse(String path);
}
