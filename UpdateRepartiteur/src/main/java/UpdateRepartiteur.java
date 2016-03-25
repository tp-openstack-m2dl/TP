package UpdateRepartiteur;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Leo on 25/03/16.
 */
public class UpdateRepartiteur {
    public static class ClientRPC {
        private XmlRpcClient client;
        public ClientRPC(String ipRepartiteur, String portRepartiteur)  throws MalformedURLException, XmlRpcException {
            // create configuration
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://"+ipRepartiteur+":"+ portRepartiteur +"/xmlrpc"));
            config.setEnabledForExtensions(true);
            config.setConnectionTimeout(60 * 1000);
            config.setReplyTimeout(60 * 1000);

            client = new XmlRpcClient();

            // use Commons HttpClient as transport
            client.setTransportFactory(
                    new XmlRpcCommonsTransportFactory(client));
            // set configuration
            client.setConfig(config);
        }
        public Object run(String command, Object[] params) throws MalformedURLException, XmlRpcException {
            return client.execute(command, params);
        }
    }
    public static void main(String[] args) throws MalformedURLException, XmlRpcException {
        if (args.length != 5) {
            throw new RuntimeException("update_repartiteur IPRepartiteur PortRepartiteur add|del Argument1 Argument2");
        }
        Object[] params = new Object[]
                { args[3], args[4] };
        ClientRPC clientRPC = new ClientRPC(args[0], args[1]);
        clientRPC.run(args[2], params);

    }
}

