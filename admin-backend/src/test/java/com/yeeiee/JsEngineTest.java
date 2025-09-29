package com.yeeiee;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.SandboxPolicy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.ByteArrayOutputStream;

/**
 * <p>
 *
 * </p>
 *
 * @author chen
 * @since 2025-09-29
 */
@Slf4j
public class JsEngineTest {
    private static final String LANGUAGE_ID = "js";
    private static final int DEFAULT_BUFFER_SIZE = 1024; // 1kb
    private static final int MAX_BUFFER_SIZE = 1024 * 1024; // 1m
    private static final String IDENTIFIER = "pm";
    private static ByteArrayOutputStream outputStream;
    private static Engine engine;

    @BeforeAll
    public static void init() {
        outputStream = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
        engine = Engine.newBuilder(LANGUAGE_ID)
                .sandbox(SandboxPolicy.CONSTRAINED)
                .logHandler(new SLF4JBridgeHandler())
                .out(outputStream)
                .err(outputStream)
                .build();
    }

    // 带参数执行脚本
    public void eval(String source, Object bindingValue) {
        try (val context = Context.newBuilder()
                .engine(engine)
                .build()) {

            if (bindingValue != null) {
                context.getBindings(LANGUAGE_ID).putMember(IDENTIFIER, bindingValue);
            }

            // eval返回value；上下文关闭，会导致拿不到value；用不上返回值，干脆不放回
            context.eval(LANGUAGE_ID, source);
        } catch (Exception e) {

            // 封装统一异常
            throw new IllegalStateException(String.format(
                    "Js 脚本执行异常: \n%s\n%s",
                    source,
                    e.getMessage()
            ));
        } finally {
            val size = outputStream.size();
            if (size > 0) {
                log.info("JS log: {}", outputStream);
            }

            // buf[] 可能会增长到一个很大的长度，如果超过最大长度，重置OutputStream，让垃圾回收
            if (outputStream.size() >= MAX_BUFFER_SIZE) {
                outputStream = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
            } else {
                // 重置指针
                outputStream.reset();
            }
        }
    }

    // 不带参数执行脚本
    public void eval(String source) {
        eval(source, null);
    }

    @Test
    public void test() {
        eval("1 + 1");
    }

    @Test
    public void test2() {
        eval("""
                        const a = 1
                        const b = 2
                        console.log(`${pm.desc}: ${a} ${pm.symbol} ${b} = ${pm.calculation(a,b)}`)""",
                new EngineMember1()
        );

        eval("""
                        const a = 1
                        const b = 2
                        console.log(`${pm.desc}: ${a} ${pm.symbol} ${b} = ${pm.calculation(a,b)}`)
                        """,
                new EngineMember2()
        );
    }

    public static class EngineMember1 {
        @HostAccess.Export
        public String desc = "addition";

        @HostAccess.Export
        public String symbol = "+";

        @HostAccess.Export
        public int calculation(int a, int b) {
            return a + b;
        }
    }

    public static class EngineMember2 {
        @HostAccess.Export
        public String desc = "subtraction";

        @HostAccess.Export
        public String symbol = "-";

        @HostAccess.Export
        public int calculation(int a, int b) {
            return a - b;
        }
    }

    @AfterAll
    public static void destroy() {
        engine.close();
    }
}
