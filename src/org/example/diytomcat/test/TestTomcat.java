package org.example.diytomcat.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.NetUtil;
import org.example.diytomcat.util.MiniBrowser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestTomcat {
    private static final int port = 18080;
    private static final String ip = "127.0.0.1";

    @BeforeClass
    public static void beforeClass() {
        // 所有测试开始前看DIY Tomcat是否已经启动了
        if (NetUtil.isUsableLocalPort(port)) {
            System.err.println("请先启动位于端口:" + port + "的DIY Tomcat, 否则无法进行单元测试.");
            System.exit(1);
        } else {
            System.out.println("检测到DIY Tomcat已经启动, 开始进行单元测试.");
        }
    }

    @Test
    public void testHelloTomcat() {
        String html = getContentString("/");
        Assert.assertEquals(html, "Hello DIY Tomcat from example.org");
    }

    @Test
    public void testHtml() {
        String html = getContentString("/a.html");
        Assert.assertEquals(html, "Hello DIY Tomcat from a.html");
    }

    @Test
    public void testTimeConsumeHtml() throws InterruptedException {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 20,
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10));
        TimeInterval timerInterval = DateUtil.timer();
        for (int i = 0; i < 3; i++) {
            threadPool.execute(() -> getContentString("/timeConsume.html"));
        }
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
        long duration = timerInterval.intervalMs();
        Assert.assertTrue(duration < 3000);
    }

    private String getContentString(String uri) {
        String url = String.format("http://%s:%s%s", ip, port, uri);
        return MiniBrowser.getContentString(url);
    }
}