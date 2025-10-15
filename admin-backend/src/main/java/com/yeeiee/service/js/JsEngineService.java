package com.yeeiee.service.js;


import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.SandboxPolicy;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * <p>
 * js 引擎
 * </p>
 *
 * @author chen
 * @since 2025-10-15
 */
@Service
@Slf4j
public class JsEngineService implements InitializingBean, DisposableBean {
    public static final String LANGUAGE_ID = "js";
    public static final int DEFAULT_BUFFER_SIZE = 100 * 1024; // 100kb
    private static Engine engine;

    @Override
    public void afterPropertiesSet() throws Exception {
        engine = Engine.newBuilder(LANGUAGE_ID)
                .sandbox(SandboxPolicy.CONSTRAINED)
                .logHandler(new SLF4JBridgeHandler())
                .out(OutputStream.nullOutputStream())
                .err(OutputStream.nullOutputStream())
                .build();
    }

    public String eval(String source){
        return eval(source, null);
    }

    /**
     * 执行js脚本
     * @param source js脚本
     * @param bindingMap 绑定参数
     * @return console.log 日志
     */
    public String eval(String source, Map<String, Object> bindingMap) {
        val outputStream = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
        try (val context = Context.newBuilder()
                .engine(engine)
                .out(outputStream)
                .err(outputStream)
                .build()) {

            if (bindingMap != null) {
                val entries = bindingMap.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    val bindings = context.getBindings(LANGUAGE_ID);
                    bindings.putMember(entry.getKey(), entry.getValue());
                }
            }
            context.eval(LANGUAGE_ID, source);
        }

        return outputStream.toString();
    }

    @Override
    public void destroy() throws Exception {
        engine.close();
    }
}
