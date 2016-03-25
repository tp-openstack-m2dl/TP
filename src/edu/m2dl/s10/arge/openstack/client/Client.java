package edu.m2dl.s10.arge.openstack.client;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import java.net.URL;

/**
 * Created by cedricrohaut on 25/03/2016.
 */
public class Client {
    public static void main(String args[]) throws Exception {
        String nbReqTemp= args[0];
        int nbReq = Integer.parseInt(nbReqTemp);
        String ipRepartiteur = args[1];
        String port = args[2];

        String url = ipRepartiteur + ":" + port;
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

        // make the a regular call
        Object[] params = new Object[]
                { new Integer(2), new Integer(3) };
        Integer result = (Integer) client.execute("Calculator.add", params);
        System.out.println("2 + 3 = " + result);

        // make a call using dynamic proxy
	  /*          ClientFactory factory = new ClientFactory(client);
          Adder adder = (Adder) factory.newInstance(Adder.class);
          int sum = adder.add(2, 4);
          System.out.println("2 + 4 = " + sum);
	  */
    }
}
