package edu.m2dl.s10.arge.openstack.calculateur;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

//  import org.apache.xmlrpc.demo.webserver.proxy.impls.AdderImpl;

public class Server {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Server needs an argument (port)");
            return;
        }

        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("(port) should be an integer");
            return;
        }

        System.out.println(String.format("Launching server on port %d", port));


        WebServer webServer = new WebServer(port);

        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

        PropertyHandlerMapping phm = new PropertyHandlerMapping();
          /* Load handler definitions from a property file.
           * The property file might look like:
           *   Calculator=org.apache.xmlrpc.demo.Calculator
           *   org.apache.xmlrpc.demo.proxy.Adder=org.apache.xmlrpc.demo.proxy.AdderImpl
           */
        phm.load(Thread.currentThread().getContextClassLoader(),
                "edu/m2dl/s10/arge/openstack/calculateur/XmlRpcServlet.properties");

          /* You may also provide the handler classes directly,
           * like this:
           * phm.addHandler("Calculator",
           *     org.apache.xmlrpc.demo.Calculator.class);
           * phm.addHandler(org.apache.xmlrpc.demo.proxy.Adder.class.getName(),
           *     org.apache.xmlrpc.demo.proxy.AdderImpl.class);
           */
        xmlRpcServer.setHandlerMapping(phm);

        XmlRpcServerConfigImpl serverConfig =
                (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);

        webServer.start();
    }
}