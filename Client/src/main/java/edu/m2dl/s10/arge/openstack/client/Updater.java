package edu.m2dl.s10.arge.openstack.client;

/**
 * Created by Leo on 12/05/16.
 */
public class Updater {
    public boolean setNbReq(int i1) {
        ThreadUpdate thread = Server.getInstance().getThread();
        thread.setNbReq(i1);
        return true;
    }
}
