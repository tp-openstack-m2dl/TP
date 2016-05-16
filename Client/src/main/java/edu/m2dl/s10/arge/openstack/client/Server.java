package edu.m2dl.s10.arge.openstack.client;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

/**
 * Created by Leo on 12/05/16.
 */
public class Server {
    private static Server instance;

    public static synchronized Server getInstance(int port, ThreadUpdate thread) {
        if (instance == null) {
            instance = new Server(port, thread);
        }
        return instance;
    }
    public static synchronized Server getInstance() {
        if (instance == null) {
            instance = new Server(0, null);
        }
        return instance;
    }
    private ThreadUpdate thread;
    private int port;
    private Server(int port, ThreadUpdate thread) {
        this.thread = thread;
        this.port = port;
    }
    public void run() throws Exception {
        System.out.println(String.format("Launching server on port %d", port));

        WebServer webServer = new WebServer(port);
        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

        PropertyHandlerMapping phm = new PropertyHandlerMapping();

        phm.addHandler("Updater", Updater.class);
        xmlRpcServer.setHandlerMapping(phm);

        XmlRpcServerConfigImpl serverConfig =
                (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);

        webServer.start();
    }
    public ThreadUpdate getThread() {
        return this.thread;
    }
}
