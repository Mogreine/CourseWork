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
        System.out.println(IBigInteger.isSimple(myInt));
        System.out.println(myInt.add(new IBigInteger(1)));
        System.out.println(defaultInt.nextProbablePrime());*/

        /*LocalTime t1 = LocalTime.now();
        Keys.KeysPair keys = Keys.genKeys(6);
        LocalTime t2 = LocalTime.now();
        System.out.println(IBigInteger.isSimple(keys.getPrivateKey()) + " " + new BigInteger(keys.getPrivateKey().toString()).isProbablePrime(55));
        System.out.println(IBigInteger.isSimple(keys.getPublicKey()) + " " + new BigInteger(keys.getPublicKey().toString()).isProbablePrime(55));
        System.out.println(keys.getPrivateKey() + " " + keys.getPublicKey());
        System.out.print("Затраченное время: " + Duration.between(t2, t1));*/
        IBigInteger a =  new IBigInteger(5L);
        IBigInteger m =  new IBigInteger(12L);
        IBigInteger x =  new IBigInteger(IBigInteger.ZERO);
        IBigInteger y =  new IBigInteger(IBigInteger.ZERO);
        IBigInteger g = IBigInteger.gcdEx(a, m, x, y);
        if (g.compareTo(IBigInteger.ONE) != 0) {
            System.out.print("Smth wrong");
        }
        else {
            x = (x.mod(m).add(m)).mod(m);
        }
        System.out.print(x);
    }

}
