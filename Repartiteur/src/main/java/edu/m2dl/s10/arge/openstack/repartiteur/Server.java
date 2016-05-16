package edu.m2dl.s10.arge.openstack.repartiteur;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Leo on 12/05/16.
 */
public class Server {


    public Long nbDiviseurs(Integer request) {
        // Choisir un calculateur
        Calculateur calculateur = Repartiteur.getInstance().getAvailableCalculateur();
        if (calculateur == null) {
            return -1L;
        }
        // CONFIGURATION DU CLIENT POUR APPELER LE CALCULATEUR DISTANT
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        Long result = -1L;
        try {
            String url = "http://"+calculateur.ip+":"+calculateur.port+"/request";
            System.out.println("Contact " + url);
            config.setServerURL(new URL(url));
            config.setEnabledForExtensions(true);
            config.setConnectionTimeout(60 * 1000);
            config.setReplyTimeout(60 * 1000);

            XmlRpcClient client = new XmlRpcClient();

            // use Commons HttpClient as transport
            client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
            // set configuration
            client.setConfig(config);

            Object[] params = new Object[] {};
            calculateur.load = (Double) client.execute("Server.getLoad", params);
            System.out.println("la charge [" + calculateur.ip + ":" + calculateur.port + "] est de " + calculateur.load);

            params = new Object[] {new Integer(request)};
            result = (Long) client.execute("Server.nbDiviseurs", params);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
