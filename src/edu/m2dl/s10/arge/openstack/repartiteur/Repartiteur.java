package edu.m2dl.s10.arge.openstack.repartiteur;

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
    Map<Calculateur> lesCalculateurs;

    @Override
    public void run() {

        while(true) {

            // Serveur : Récupérer les requetes des clients


            // Serveur : Récupérer les requetes de l'updateRepartiteur


            // Client : Contacter les Calculateur

        }

    }

    public Repartiteur(String port) {
        this.port = port;
    }

    public void add(String ip, String port) {

    }

    public void del(String ip, String port) {

    }
}
