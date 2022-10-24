package cn.how2j.diytomcat.test;

import cn.how2j.diytomcat.Bootstrap;
import cn.how2j.diytomcat.util.MiniBrowser;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Zheng Xin
 * @since 2022/10/7 22:10
 */
public class TestTomcat {
    private static final int port = 18080;
    private static final String ip = "127.0.0.1";

    @BeforeClass
    public static void beforeClass() {
        String[] args = new String[1];
        new Thread(() -> Bootstrap.main(args)).start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 所有测试开始前看diy tomcat 是否已经启动了
        if (NetUtil.isUsableLocalPort(port)) {
            System.err.println("请先启动 位于端口: " + port + " 的diy tomcat，否则无法进行单元测试");
            System.exit(1);
        }
        System.out.println("检测到diy tomcat已经启动,开始进行单元测试");
    }

    @Test
    public void testHelloTomcat() {
        String html = getContentString("/");
        Assert.assertEquals(html, "Hello DIY Tomcat from how2j.cn");
        System.out.println("回归测试通过");
    }

    @Test
    public void testTimeConsumeHtml() throws InterruptedException {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 20, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10));
        TimeInterval timeInterval = DateUtil.timer();

        for (int i = 0; i < 3; i++) {
            threadPool.execute(() -> getContentString("/timeConsume.html"));
        }
        // 尝试关闭线程池，但是如果 线程池里有任务在运行，就不会强制关闭，直到任务都结束了，才关闭
        threadPool.shutdown();
        // 给线程池1个小时的时间去执行，如果超过1个小时了也会返回，如果在一个小时内任务结束了，就会马上返回
        boolean shutdownResult = threadPool.awaitTermination(1, TimeUnit.HOURS);
        long duration = 0;
        if (shutdownResult) {
            duration = timeInterval.intervalMs();
        }

        Assert.assertTrue(duration < 3000);
    }

    @Test
    public void testAIndex() {
        String html = getContentString("/a/index.html");
        Assert.assertEquals(html, "Hello DIY Tomcat from index.html@a");
    }

    @Test
    public void testBIndex() {
        String html = getContentString("/b/index.html");
        Assert.assertEquals(html, "Hello DIY Tomcat from index.html@b");
    }

    private String getContentString(String uri) {
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        return MiniBrowser.getContentString(url);
    }
}
