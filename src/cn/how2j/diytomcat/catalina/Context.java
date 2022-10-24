package cn.how2j.diytomcat.catalina;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.log.LogFactory;

/**
 * 代表一个应用
 *
 * @author Zheng Xin
 * @since 2022/10/10
 */
public class Context {
    /**
     * 表示访问的路径
     */
    private String path;

    /**
     * 表示对应在文件系统中的位置
     */
    private String docBase;

    public Context(String path, String docBase) {
        TimeInterval timeInterval = DateUtil.timer();
        this.path = path;
        this.docBase = docBase;
        LogFactory.get().info("Deploying web application directory {}", this.docBase);
        LogFactory.get().info("Deployment of web application directory {} has finished in {} ms", this.docBase, timeInterval.intervalMs());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDocBase() {
        return docBase;
    }

    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }
}
