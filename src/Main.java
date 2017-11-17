import ArbitraryPrecisionArithmetic.IBigInteger;
import RSA.Keys;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {
        /*BigInteger defaultInt = new BigInteger("9795684123579518357551122165468797541");
        IBigInteger myInt = new IBigInteger("9795684123579518357551122165468797541");
        int pow = 1000;
        System.out.println(defaultInt.isProbablePrime(55));
        System.out.println(IBigInteger.isPrime(myInt));
        System.out.println(myInt.add(new IBigInteger(1)));
        System.out.println(defaultInt.nextProbablePrime());*/

        /*long t1 = System.currentTimeMillis();
        Keys.KeysPair keys = Keys.genKeys(27);
        long t2 = System.currentTimeMillis();
        System.out.println(IBigInteger.isPrime(keys.getPrivateKey()) + " " + new BigInteger(keys.getPrivateKey().toString()).isProbablePrime(55));
        System.out.println(IBigInteger.isPrime(keys.getPublicKey()) + " " + new BigInteger(keys.getPublicKey().toString()).isProbablePrime(55));
        System.out.println(keys.getPrivateKey() + " " + keys.getPublicKey());
        System.out.print("Затраченное время: " + ((double)t2 - t1) / 1000);*/

        IBigInteger a =  new IBigInteger(7);
        IBigInteger m =  new IBigInteger(12);
        IBigInteger x =  new IBigInteger(IBigInteger.ZERO);
        IBigInteger y =  new IBigInteger(IBigInteger.ZERO);
        IBigInteger g = IBigInteger.gcdEx(a, m, x, y);
        if (g.compareTo(IBigInteger.ONE) != 0) {
            System.out.print("Smth wrong");
        }
        else {
            x = (x.mod(m).add(m)).mod(m);
        }
        System.out.print(x + " " + y + " " + (Integer.parseInt(a.toString()) * Integer.parseInt(x.toString())) % Integer.parseInt(m.toString()));
    }

}
