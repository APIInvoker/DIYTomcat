package cn.how2j.diytomcat;

import cn.how2j.diytomcat.catalina.Server;

/**
 * 自定义的Tomcat
 *
 * @author Zheng Xin
 * @since 2022/10/8 22:42
 */
public class Bootstrap {
    public static void main(String[] args) {
        new Server().start();
    }
}
