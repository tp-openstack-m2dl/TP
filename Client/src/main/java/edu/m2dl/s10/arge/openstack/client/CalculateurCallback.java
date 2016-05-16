package edu.m2dl.s10.arge.openstack.client;

import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;

/**
 * Created by Leo on 16/05/16.
 */
public class CalculateurCallback implements AsyncCallback {

    @Override
    public void handleError(XmlRpcRequest request, Throwable t) {
        System.out.println("In error");
        t.printStackTrace();

    }

    @Override
    public void handleResult(XmlRpcRequest request, Object result) {
        System.out.println("Nombre de diviseurs = "+result);
    }
}
