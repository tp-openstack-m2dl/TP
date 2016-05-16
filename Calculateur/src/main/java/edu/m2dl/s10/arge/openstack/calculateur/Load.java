package edu.m2dl.s10.arge.openstack.calculateur;

import oshi.SystemInfo;

import javax.management.MBeanServerConnection;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * Created by Leo on 13/05/16.
 */
public class Load {

    private static Load instance;

    public static synchronized Load getInstance() {
        if (instance == null) {
            try {
                instance = new Load();
            } catch (Exception e) {
                System.out.println("Failed to create instance");
                e.printStackTrace();
            }
        }
        return instance;
    }
    private Load() throws Exception{
        this.systemInfo = new SystemInfo();
    }

    private SystemInfo systemInfo;
    // Merci benoit pour avoir deniché cette lib
    public double getLoad() {
        return this.systemInfo.getHardware().getProcessor().getSystemCpuLoad();
    }
}
