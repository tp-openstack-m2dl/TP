package edu.m2dl.s10.arge.openstack.calculateur;

public class Calculateur {
    public int add(int i1, int i2) {
        return i1 + i2;
    }

    public int subtract(int i1, int i2) {
        return i1 - i2;
    }

    public Long nbDiviseurs(int number) {
        Double myBigNumber = Math.pow(2, number) + 1;
        Long result = 0L;
        for(int n = 1 ; n <= myBigNumber; n++)
            if(myBigNumber % n == 0)
                result++;
        System.out.println(Integer.toString(number) + " => nbDiviseurs " + Double.toString(myBigNumber) + " = " + result);
        return result;
    }
    public Double getLoad() {
        Double val = Load.getInstance().getLoad();
        System.out.println("Charge CPU " + val.toString());
        return val;
    }
}