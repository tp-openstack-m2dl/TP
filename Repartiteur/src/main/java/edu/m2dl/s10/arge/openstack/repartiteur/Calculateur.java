package edu.m2dl.s10.arge.openstack.repartiteur;

/**
 * Created by Leo on 12/05/16.
 */
public class Calculateur {
    public String ip;
    public String port;
    public String uid;
    public Double load;

    public Calculateur(String ip, String port, String uid) {
        this.ip = ip;
        this.port = port;
        this.uid = uid;
        this.load = 0D;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getUid() {
        return uid;
    }

    public Double getLoad() {
        return load;
    }
}
