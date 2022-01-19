package org.example.diytomcat.util;

import cn.hutool.system.SystemUtil;

import java.io.File;

public class Constant {
    public final static String RESPONSE_HEAD_202 =
            """
                    HTTP/1.1 200 OK\r
                    Content-Type: {}\r
                    \r
                    """;
    public final static File WEBAPPS_FOLDER = new File(SystemUtil.get("user.dir"), "webapps");
    public final static File ROOT_FOLDER = new File(WEBAPPS_FOLDER, "ROOT");
}