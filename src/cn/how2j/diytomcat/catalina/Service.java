package cn.how2j.diytomcat.catalina;

import cn.how2j.diytomcat.util.ServerXMLUtil;

/**
 * @author Zheng Xin
 * @since 2022/10/24
 */
public class Service {
    private String name;
    private Engine engine;
    private Server server;

    public Service(Server server) {
        this.server = server;
        this.name = ServerXMLUtil.getServiceName();
        this.engine = new Engine(this);
    }

    public Engine getEngine() {
        return engine;
    }

    public Server getServer() {
        return server;
    }
}