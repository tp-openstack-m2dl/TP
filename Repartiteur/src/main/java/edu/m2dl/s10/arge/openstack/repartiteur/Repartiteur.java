package edu.m2dl.s10.arge.openstack.repartiteur;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import java.io.IOException;
import java.util.Map;

/**
 * Created by julien on 25/03/16.
 */
public class Repartiteur implements Runnable {
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Il faut fournir le numéro du port");
        }

        String port = args[0];

        // Boucle de reception des requetes

        Repartiteur r = new Repartiteur(port);
        r.run();

    }

    private String port;

    public void run() {

        WebServer webServer = new WebServer(Integer.parseInt(port));

        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

        PropertyHandlerMapping phm = new PropertyHandlerMapping();
          /* Load handler definitions from a property file.
           * The property file might look like:
           *   Calculator=org.apache.xmlrpc.demo.Calculator
           *   org.apache.xmlrpc.demo.proxy.Adder=org.apache.xmlrpc.demo.proxy.AdderImpl
           */
        try {
            phm.load(Thread.currentThread().getContextClassLoader(),
                    "XmlRpcServlet.properties");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

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

        try {
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


        while(true) {



            // Serveur : Récupérer les requetes de l'updateRepartiteur


            // Client : Contacter les Calculateur

        }

    }

    public Repartiteur(String port) {
        this.port = port;
    }

    public void add(String ip, String port) {
        System.out.println("AJOUTE UN CALCULTEUR ["+ip+":"+port+"]");

    }

    public void del(String ip, String port) {
        // Appel de Léo
        System.out.println("SUPPRIME UN CALCULTEUR ["+ip+":"+port+"]");

    }

    public void request() {
        // Appel du client
        System.out.println("REDIRIGE LA REQUETE VERS UN CALCULATEUR");
    }
}
