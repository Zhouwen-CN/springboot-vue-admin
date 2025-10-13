package com.yeeiee.config;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.FormattedLogger;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * P6spySQL日志格式化
 * </p>
 *
 * @author chen
 * @since 2025-08-18
 */
@Slf4j
public class P6spySqlFormatConfiguration extends FormattedLogger {

    private static final Set<Category> INCLUDE = new HashSet<>();

    static {
        INCLUDE.add(Category.STATEMENT);
        INCLUDE.add(Category.BATCH);
    }

    @Override
    public void logException(Exception e) {
        // do noting
    }

    @Override
    public void logText(String text) {
        // TODO 考虑放弃打印sql，或者不用自动装配（定制化），quartz打印太多日志了
        // log.info(text.replaceAll("\\s+", StringUtils.SPACE));
    }

    @Override
    public boolean isCategoryEnabled(Category category) {
        return INCLUDE.contains(category);
    }
}
