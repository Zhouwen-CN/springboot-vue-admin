package com.yeeiee;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.search.Search;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.Data;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *  {@code @Timed} 耗时分布统计 Prometheus 分位数指标
 *  {@code @Counted} 调用次数/错误率 Grafana 请求量仪表盘
 *  {@code @MeterTag} 动态维度标签 多维度下钻分析
 * </pre>
 *
 * @author chen
 * @since 2025-08-13
 */
public final class MicrometerTest {

    private static DateTimeFormatter formatter;
    private static Random random;

    @Data
    public static class Order {
        private String orderId;
        private Integer amount;
        private String channel;
        private LocalDateTime createTime;
    }

    @BeforeAll
    public static void init() {
        Metrics.addRegistry(new SimpleMeterRegistry());
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        random = new Random();
    }

    @Test
    public void CounterTest() {
        Order order1 = new Order();
        order1.setOrderId("ORDER_ID_1");
        order1.setAmount(100);
        order1.setChannel("CHANNEL_A");
        order1.setCreateTime(LocalDateTime.now());
        createOrder(order1,null);

        Order order2 = new Order();
        order2.setOrderId("ORDER_ID_2");
        order2.setAmount(200);
        order2.setChannel("CHANNEL_B");
        order2.setCreateTime(LocalDateTime.now());
        createOrder(order2,null);
    }

    private void createOrder(Order order, Duration delay) {
        if (delay != null) {
            try {
                TimeUnit.MILLISECONDS.sleep(delay.toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }else{
            Metrics.counter("order.create",
                    "channel", order.getChannel(),
                    "createTime", formatter.format(order.getCreateTime())).increment();
        }
    }

    @Test
    public void TimerTest() {
        Order order1 = new Order();
        order1.setOrderId("ORDER_ID_1");
        order1.setAmount(100);
        order1.setChannel("CHANNEL_A");
        order1.setCreateTime(LocalDateTime.now());
        Timer timer = Metrics.timer("timer",
                "createOrder", "cost"
        );
        timer.record(() -> createOrder(order1,Duration.ofSeconds(random.nextInt(5))));
        timer.record(() -> createOrder(order1,Duration.ofSeconds(random.nextInt(5))));
    }


    @Test
    public void GaugeTest() {
        val atomicInteger = new AtomicInteger(0);
        Metrics.gauge("gauge.test", atomicInteger, AtomicInteger::doubleValue);

        atomicInteger.incrementAndGet();
        atomicInteger.decrementAndGet();
        atomicInteger.incrementAndGet();
        atomicInteger.incrementAndGet();
    }

    @AfterEach
    public void print() {
        Search.in(Metrics.globalRegistry).meters().forEach(each -> {
            String builder = "\nname:\t" +
                    each.getId().getName() +
                    "\ntags:\t" +
                    each.getId().getTags() +
                    "\ntype:\t" + each.getId().getType() +
                    "\nvalue:\t" + each.measure();
            System.out.println(builder);
        });
    }
}
