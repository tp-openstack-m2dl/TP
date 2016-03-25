package edu.m2dl.s10.arge.openstack.repartiteur;

/**
 * Created by julien on 25/03/16.
 */
public class Repartiteur extends Runnable {
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
    List<Calculateur> lesCalculateurs;

    @Override
    public void run() {

    }

    public Repartiteur(String port) {
        this.port = port;
    }
}
