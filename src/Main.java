import ArbitraryPrecisionArithmetic.IBigInteger;
import RSA.AlgorithmRSA;

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
        Keys.KeysPair keys = Keys.genPrimeNumbers(27);
        long t2 = System.currentTimeMillis();
        System.out.println(IBigInteger.isPrime(keys.getN2()) + " " + new BigInteger(keys.getN2().toString()).isProbablePrime(55));
        System.out.println(IBigInteger.isPrime(keys.getN1()) + " " + new BigInteger(keys.getN1().toString()).isProbablePrime(55));
        System.out.println(keys.getN2() + " " + keys.getN1());
        System.out.print("Затраченное время: " + ((double)t2 - t1) / 1000);*/

        /*int aa = 257, mm = 48;
        IBigInteger a =  new IBigInteger(aa);
        IBigInteger m =  new IBigInteger(mm);
        IBigInteger x =  new IBigInteger(IBigInteger.ZERO);
        IBigInteger y =  new IBigInteger(IBigInteger.ZERO);
        IBigInteger g = IBigInteger.gcdEx(a, m, x, y);
        if (g.compareTo(IBigInteger.ONE) != 0) {
            System.out.print("Smth wrong");
        }
        else {
            x = (x.mod(m).add(m)).mod(m);
        }
        System.out.print(x + " " + (aa * Integer.parseInt(x.toString())) % mm);*/

        AlgorithmRSA user1 = new AlgorithmRSA(6);
        AlgorithmRSA user2 = new AlgorithmRSA(6);
        IBigInteger message = new IBigInteger(132L);

        for (int i = 0; i < 10; i++) {
            String encodedLetter = user1.encoding(message, user2.getPublicKey());
            String decodedLetter = user2.decoding(new IBigInteger(encodedLetter));
            user1 = new AlgorithmRSA(6);
            user2 = new AlgorithmRSA(6);
            System.out.println(decodedLetter);
        }

        /*String encodedLetter = user1.encoding(message, user2.getPublicKey());
        String decodedLetter = user2.decoding(new IBigInteger(encodedLetter));
        System.out.print(decodedLetter);*/

        /*IBigInteger a = new IBigInteger(-1);
        IBigInteger b = new IBigInteger(0);
        System.out.print(a.mul(b));*/
    }

}
