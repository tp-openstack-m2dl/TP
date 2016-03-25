package updateClient;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Abel on 25/03/16.
 */
public class UpdateClient {

    private XmlRpcClient client;

    public UpdateClient(String ipClient, String portClient)  throws MalformedURLException, XmlRpcException {
        // create configuration
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://"+ipClient+":"+ portClient +"/xmlrpc"));
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

    public static void main(String[] args) throws Exception {
        if(args.length != 4)
            throw new RuntimeException("missing arguments, Syntaxe: update_client param addresse port");

        UpdateClient updater = new UpdateClient(args[3],args[4]);

        Object[] params = new Object[]
                { new Integer(args[1]), args[2] };
        if(!(Boolean) updater.run("update", params))
            throw new RuntimeException("Failed to Update client");


    }
}
