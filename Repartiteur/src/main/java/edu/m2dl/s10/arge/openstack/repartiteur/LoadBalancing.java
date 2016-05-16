package edu.m2dl.s10.arge.openstack.repartiteur;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by Leo on 16/05/16.
 */
public class LoadBalancing extends Thread {
    public LoadBalancing(String name){
        super(name);
    }

    public void run() {
        try {
            sleep(10000);
        } catch(Exception e) {

        }
        while (true) {
            try {
                List<Calculateur> calculateurs = Repartiteur.getInstance().getCalculateurs();
                double chargeMoyenne = 0D;
                int idCalculateurInutilise = 0;
                double chargePlusBasse = 100D;
                for (int i = 0; i < calculateurs.size(); i++) {
                    Calculateur calculateur = calculateurs.get(i);
                    try {
                        calculateur.load = this.getCharge(calculateur);
                        System.out.println("la charge [" + calculateur.ip + ":" + calculateur.port + "] est de " + calculateur.load);
                        chargeMoyenne += calculateur.load;
                        if (chargePlusBasse > calculateur.load) {
                            idCalculateurInutilise = i;
                        }
                    } catch (Exception e) {
                        // Des problemec avec ce calculateur, on le détruit
                        e.printStackTrace();
                        if (calculateur != null) {
                            System.out.println("Le container " + calculateur.ip + " ne repond pas");
                            //this.supprimerVM(calculateur);
                        }
                    }
                }
                chargeMoyenne = chargeMoyenne / calculateurs.size();
                System.out.println("Charge moyenne " + chargeMoyenne + " idCalculateurInutilise " + idCalculateurInutilise);
                if (chargeMoyenne > 0.7D && calculateurs.size() < 3) {
                    OpenStackService ops = new OpenStackService();
                    Calculateur calculateur = ops.addVM();
                    calculateurs.add(calculateur);
                }
                if (chargeMoyenne < 0.2D && calculateurs.size() > 1) {
                    this.supprimerVM(calculateurs.get(idCalculateurInutilise));
                }
                sleep(5000);
            } catch (Exception e) {
                System.out.println("Load balancing error");
                e.printStackTrace();
            }
        }
    }
    public void supprimerVM(Calculateur calculateur) {
        Repartiteur.getInstance().removeCalculateur(calculateur);
        OpenStackService ops = new OpenStackService();
        ops.deleteVM(calculateur);
    }
    public Double getCharge(Calculateur calculateur) throws Exception {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

        String url = "http://"+calculateur.ip+":"+calculateur.port+"/request";
        System.out.println("Contact " + url);
        config.setServerURL(new URL(url));
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(30 * 1000);
        config.setReplyTimeout(30 * 1000);

        XmlRpcClient client = new XmlRpcClient();

        // use Commons HttpClient as transport
        client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
        // set configuration
        client.setConfig(config);

        Object[] params = new Object[] {};
        return (Double) client.execute("Server.getLoad", params);
    }
}