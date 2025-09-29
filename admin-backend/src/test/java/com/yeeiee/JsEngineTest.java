package com.yeeiee;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.SandboxPolicy;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;

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
    public static final String LANGUAGE_ID = "js";
    public static final int DEFAULT_BUFFER_SIZE = 1024; // 1kb
    public static final int MAX_BUFFER_SIZE = 1024 * 1024; // 1m
    public static final String IDENTIFIER = "pm";
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
    public void eval(String source, Object bindingValue, Consumer<Value> consumer) {
        try (val context = Context.newBuilder()
                .engine(engine)
                .build()) {

            if (bindingValue != null) {
                context.getBindings(LANGUAGE_ID).putMember(IDENTIFIER, bindingValue);
            }

            // eval返回value，上下文关闭，会导致拿不到value，这里干脆不返回
            val value = context.eval(LANGUAGE_ID, source);
            if (consumer != null) {
                consumer.accept(value);
            }
        } finally {
            log.info("JS log: {}", outputStream);
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
    public void eval(String source, Consumer<Value> consumer) {
        eval(source, null, consumer);
    }

    public void eval(String source) {
        eval(source, null);
    }


    @Test
    public void test() {
        eval("1 + 1",value -> System.out.println("value = " + value));
    }

    public static class EngineMember {
        @HostAccess.Export
        public String name = "add";

        @HostAccess.Export
        public int add(int a, int b) {
            return a + b;
        }
    }

    @Test
    public void test2() {
        // eval("""
        //                 const a = 1
        //                 const b = 2
        //                 console.log(`${pm.name}: ${a} + ${b} = ${pm.add(a,b)}`)
        //                 """,
        //         new EngineMember()
        // );
        //
        // val engineMember = new EngineMember();
        // engineMember.name = "sub";
        // eval("""
        //                 const a = 1
        //                 const b = 2
        //                 console.log(`${pm.name}: ${a} + ${b} = ${pm.add(a,b)}`)
        //                 """,
        //         engineMember
        // );
    }

    @AfterAll
    public static void destroy() {
        engine.close();
    }
}
