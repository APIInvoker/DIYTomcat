package cn.how2j.diytomcat.test;

import cn.how2j.diytomcat.Bootstrap;
import cn.how2j.diytomcat.util.MiniBrowser;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Zheng Xin
 * @since 2022/10/8 22:42
 */
public class TestHtml {
    private static final int port = 18080;
    private static final String ip = "127.0.0.1";

    @BeforeClass
    public static void beforeClass() {
        String[] args = new String[1];
        new Thread(() -> Bootstrap.main(args)).start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 所有测试开始前看diy tomcat 是否已经启动了
        if (NetUtil.isUsableLocalPort(port)) {
            System.err.println("请先启动 位于端口: " + port + " 的diy tomcat，否则无法进行单元测试");
            System.exit(1);
        } else {
            System.out.println("检测到 diy tomcat已经启动，开始进行单元测试");
        }
    }

    @Test
    public void testHelloTomcat() {
        String html = getContentString("/");
        Assert.assertEquals(html, "Hello DIY Tomcat from how2j.cn");
    }

    @Test
    public void testHtml() {
        String html = getContentString("/a.html");
        Assert.assertEquals(html, "Hello DIY Tomcat from a.html");
    }

    private String getContentString(String uri) {
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        return MiniBrowser.getContentString(url);
    }
}
