package com.yeeiee.scheduler.handler;

/**
 * <p>
 * 任务处理器
 * </p>
 *
 * @author chen
 * @since 2025-10-16
 */
public interface JobHandler {
    /**
     * jobHandlerName
     * @return jobHandlerName
     */
    String name();

    /**
     * 执行job
     * @param param jobHandlerParam
     * @return result
     */
    String execute(String param);
}
