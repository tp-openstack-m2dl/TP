package edu.m2dl.s10.arge.openstack.client;

import java.util.Scanner;

/**
 * Created by cedricrohaut on 25/03/2016.
 */
public class ThreadUpdate extends Thread {
    private int nbReq;

    public int getNbReq() {
        return nbReq;
    }

    public int getWaitTimeBetweenRequest() {
        return 1000 / nbReq;
    }
    public void setNbReq(int nbReq) {
        this.nbReq = nbReq;
    }

    public ThreadUpdate(String name){
        super(name);
        this.nbReq = 1;
    }

    public void run(){
        System.out.println(this.getName());


        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("T1 - Veuillez saisir un nombre de requêtes :");
            nbReq = sc.nextInt();
            System.out.println("T1 - Vous avez saisi : " + nbReq);
        }
    }
}