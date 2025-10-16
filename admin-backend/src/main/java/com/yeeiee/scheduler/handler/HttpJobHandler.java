package com.yeeiee.scheduler.handler;

import org.springframework.stereotype.Service;

/**
 * <p>
 * http任务处理类
 * </p>
 *
 * @author chen
 * @since 2025-10-16
 */
@Service
public class HttpJobHandler implements JobHandler {
    @Override
    public String name() {
        return "http";
    }

    @Override
    public String execute(String param) {
        return "this is httpHandler";
    }
}
