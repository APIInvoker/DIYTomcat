package org.example.diytomcat.test;

import org.example.diytomcat.util.MiniBrowser;
import cn.hutool.core.util.NetUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

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
    public void testaHtml() {
        String html = getContentString("/a.html");
        Assert.assertEquals(html,"Hello DIY Tomcat from a.html");
    }

    private String getContentString(String uri) {
        String url = String.format("http://%s:%s%s", ip, port, uri);
        return MiniBrowser.getContentString(url);
    }
}