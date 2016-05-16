package edu.m2dl.s10.arge.openstack.repartiteur;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import java.io.*;
import java.util.*;

/**
 * Created by julien on 25/03/16.
 */
public class Repartiteur {

    public static void main(String[] args) throws XmlRpcException {
        if(args.length != 1) {
            System.out.println("Erreur dans les arguments. Ajoutez un port");
        }

        String port = args[0];

        // Crée un obj répartiteur en mode singleton
        // et lance une VM de calculateur si le port est différent de 3654
        Repartiteur r = Repartiteur.getInstance(port);

        // Lance le thread qui surveille la charge des calculateurs
        LoadBalancing loadBalancing = new LoadBalancing("loadbalancing");
        loadBalancing.start();

        // Lancement du serveur
        WebServer webServer = new WebServer(new Integer(port));
        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        phm.addHandler("Server", Server.class);
        xmlRpcServer.setHandlerMapping(phm);
        XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);

        try {
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum MODES {
        PROD,
        LOCAL
    };
    private MODES mode;
    private String port;
    private static List<Calculateur> calculateurs = new ArrayList();
    private static Repartiteur instance;

    public static synchronized Repartiteur getInstance(String port) {
        if (instance == null) {
            instance = new Repartiteur(port);
        }
        return instance;
    }
    public static synchronized Repartiteur getInstance() {
        return instance;
    }

    private Repartiteur(String port) {
        if (port.equals("3654")) {
            System.out.println("* * * LOCAL MODE / NO VMS WILL BE CREATED / DEV PORT " + port);
            // cas spécial, on est en local
            this.mode = MODES.LOCAL;
        } else {
            this.mode = MODES.PROD;
        }
        if(mode.equals(MODES.PROD)) {
            System.out.println("Lancement d'un premier calculateur ");
            OpenStackService ops = new OpenStackService();
            Calculateur cal = ops.addVM();

            // AJOUT D'UN PREMIER CALCULATEUR
            calculateurs.add(cal);
            System.out.println(cal);
        }
    }
    public Boolean add(String ip, String port) {


        Calculateur calculateurLocal = null;

        if(mode.equals(MODES.PROD)) {
            OpenStackService ops = new OpenStackService();
            calculateurLocal = ops.addVM();
        }

        if(mode.equals(MODES.LOCAL)) {
            calculateurLocal = new Calculateur("localhost", port, null);
        }

        calculateurs.add(calculateurLocal);
        System.out.println("ADD ["+calculateurLocal.ip+", "+ calculateurs.size()+" calculateurs]");
        return true;
    }


    public Calculateur getAvailableCalculateur() {
        if (calculateurs.size() > 0) {
            Random rand = new Random();
            int random = rand.nextInt(calculateurs.size());
            System.out.println("Use the "+random +" calcultateur");
            return calculateurs.get(random);
        }
        return null;
    }

    public List<Calculateur> getCalculateurs() {
        return calculateurs;
    }

    public void setCalculateurs(List<Calculateur> calculateurs) {
        Repartiteur.calculateurs = calculateurs;
    }
    public void removeCalculateur(Calculateur c) {
        List<Calculateur> calculateurs = this.getCalculateurs();

        for (Iterator<Calculateur> iterator = calculateurs.iterator(); iterator.hasNext();) {
            Calculateur calculateur = iterator.next();
            if (calculateur.port.equals(c.getPort()) &&
                    calculateur.ip.equals(c.getIp())) {
                iterator.remove();
            }
        }
        this.setCalculateurs(calculateurs);
    }
}