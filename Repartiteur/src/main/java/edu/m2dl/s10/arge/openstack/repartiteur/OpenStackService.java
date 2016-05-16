package edu.m2dl.s10.arge.openstack.repartiteur;

import org.apache.commons.codec.binary.Base64;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.openstack.OSFactory;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by Leo on 13/05/16.
 */
public class OpenStackService {

    private static final String imageId = "a342d400-75f0-4a85-b12c-bab596eb7d72";
    public static final String networkId = "c1445469-4640-4c5a-ad86-9c0cb6650cca";
    private OSClient os;
    private String username = "ens18";
    private String password = "LEBWJ1";
    private String portCalculateur = "2012";
    private String versionCalculateur = "2.0";
    public OpenStackService() {
        System.out.println("Tentative de connexion à l'API CLOUDMIP");
        String url = "http://195.220.53.61:5000/v2.0";
        os = OSFactory.builder()
                .endpoint(url)
                .credentials(this.username, this.password)
                .tenantName("service")
                .authenticate();

        System.out.println("Connexion à l'API CLOUDMIP OK");

    }
    // Ce script permet de lancer un repartiteur sur Openstack
    public String getUserDataForCalculateur() {
        return "#!/bin/bash\r\n" +
        "curl -o calculateur.jar https://s3.eu-central-1.amazonaws.com/m2dl-api-smartpaulo/ARGE/arge.openstack.calculateur-"+versionCalculateur+"-SNAPSHOT-jar-with-dependencies.jar\r\n" +
        "java -jar calculateur.jar " + portCalculateur + " > log_calculateur.txt";
    }
    public Calculateur addVM() {
        System.out.println("Creation d'une nouvelle instance de calculateur sur le cloud");

        ServerCreate sc;
        System.out.println(this.getUserDataForCalculateur());
        List networksId = Arrays.asList(networkId);
        sc = Builders.server().name("lmieu_calculateur_" + new Date().getTime())
                .flavor("2") // SMALL
                .image(imageId)
                .addSecurityGroup("AderFber_calculator") // une regle de securité qui ouvre le port 2012
                .keypairName("leokey")
                .networks(networksId)
                .userData(DatatypeConverter.printBase64Binary(this.getUserDataForCalculateur().getBytes()))
                .build();

        Server server = os.compute().servers().boot(sc);

        if (server.getStatus() == Server.Status.ERROR) {
            System.out.println("Erreur lors de la creation du calculateur");
        }

        // En attente du boot de la VM
        while ((server = os.compute().servers().get(server.getId())).getStatus() != Server.Status.ACTIVE) {
            try {
                System.out.println("Le serveur " + server.getId() + " n'est toujours pas lancé");
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                System.out.println("Le serveur " + server.getId() + " n'a pas réussi à boot");
                e.printStackTrace();
            }
        }

        // Récupération de l'IP de la machine
        Map<String, List<? extends Address>> adresses = server.getAddresses().getAddresses();
        if (adresses.size() <= 0) {
            System.out.println("Le serveur " + server.getId() + " n'a pas d'adresse réseau");
        }

        Address address = adresses.values().iterator().next().get(0);
        System.out.println("Le serveur " + server.getId() + " est lancé a l'adresse " + address.getAddr() + " port " + portCalculateur);

        Calculateur calculateur = new Calculateur(address.getAddr(), portCalculateur, server.getId());
        return calculateur;

    }

    public boolean deleteVM(Calculateur calculateur) {
        System.out.println("Suppression du calculateur [" + calculateur.ip + ", " + calculateur.uid + "]");
        os.compute().servers().delete(calculateur.uid);
        return true;
    }

}

