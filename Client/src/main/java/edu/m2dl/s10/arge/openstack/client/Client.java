package edu.m2dl.s10.arge.openstack.client;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.ServletWebServer;
import org.apache.xmlrpc.webserver.WebServer;

import java.net.URL;

import static java.lang.Thread.sleep;

/**
 * Created by cedricrohaut on 25/03/2016.
 */
public class Client {
    private static int nbReq;
    private static String ipRepartiteur;
    private static String port;

    public Client(int nbReq, String ipRepartiteur, String port) {
        this.nbReq = nbReq;
        this.ipRepartiteur = ipRepartiteur;
        this.port = port;
    }

    public int getNbReq() {
        return nbReq;
    }

    public void setNbReq(int nbReq) {
        this.nbReq = nbReq;
    }

    public String getIpRepartiteur() {
        return ipRepartiteur;
    }

    public void setIpRepartiteur(String ipRepartiteur) {
        this.ipRepartiteur = ipRepartiteur;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public static void main(String args[]) throws Exception {

        if (args.length != 3) {
            throw new ServletWebServer.Exception(1, "\nErreur dans les arguments : nombreRequetes ipRepartiteur port", "nombreRequetes ipRepartiteur port");
        }
        String nbReqTemp= args[0];
        nbReq = Integer.parseInt(nbReqTemp);
        ipRepartiteur = args[1];
        port = args[2];

        System.out.println("Le nom du thread principal est " + Thread.currentThread().getName());

        ThreadUpdate t = new ThreadUpdate("ThreadUpdate");
        t.start();
        Server.getInstance(19000, t).run();

        String url = "http://" + ipRepartiteur + ":" + port + "/request";
        System.out.println(url);

        // create configuration
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(url));
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(60 * 1000);
        config.setReplyTimeout(60 * 1000);

        XmlRpcClient client = new XmlRpcClient();

        // use Commons HttpClient as transport
        client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
        // set configuration
        client.setConfig(config);
        t.setNbReq(nbReq);
        // Entier dont on va calculer le nombre de diviseurs
        int i = 0 ;
        while (true) {
            // make the a regular call
            Object[] params = new Object[] { new Integer(i)};
            client.executeAsync("Server.nbDiviseurs", params, new CalculateurCallback());
            sleep(t.getWaitTimeBetweenRequest());
            i++;
            if (i > 50) {
                i -= 25;
            }
        }
    }

}
