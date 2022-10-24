package cn.how2j.diytomcat.util;

import cn.hutool.system.SystemUtil;

import java.io.File;

/**
 * 常量类
 *
 * @author Zheng Xin
 * @since 2022/9/29 22:09
 */
public class Constant {
    public final static String response_head_202 =
            "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: {}\r\n\r\n";

    static String parent = SystemUtil.get("user.dir");

    public final static File webappsFolder = new File(parent, "webapps");
    public final static File rootFolder = new File(webappsFolder, "ROOT");

    public static final File confFolder = new File(SystemUtil.get("user.dir"), "conf");
    public static final File serverXmlFile = new File(confFolder, "server.xml");
}
