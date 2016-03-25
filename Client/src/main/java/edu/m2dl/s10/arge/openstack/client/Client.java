package edu.m2dl.s10.arge.openstack.client;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import java.net.URL;

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
        String nbReqTemp= args[0];
        nbReq = Integer.parseInt(nbReqTemp);
        ipRepartiteur = args[1];
        port = args[2];

        System.out.println("Le nom du thread principal est " + Thread.currentThread().getName());

        ThreadUpdate t = new ThreadUpdate("ThreadUpdate");
        t.start();

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
        client.setTransportFactory(
                new XmlRpcCommonsTransportFactory(client));
        // set configuration
        client.setConfig(config);

        while (true) {
            nbReq = t.getNbReq();
            //System.out.println("Main - nombre de requêtes " + nbReq);

            for (int i = 0 ; i < nbReq ; ++i) {
                // make the a regular call
                Object[] params = new Object[]
                        { new Integer(2), new Integer(3) };
                Integer result = (Integer) client.execute("Calculator.add", params);
                System.out.println("2 + 3 = " + result);
            }
        }


        // make a call using dynamic proxy
	  /*          ClientFactory factory = new ClientFactory(client);
          Adder adder = (Adder) factory.newInstance(Adder.class);
          int sum = adder.add(2, 4);
          System.out.println("2 + 4 = " + sum);
	  */
    }
}
