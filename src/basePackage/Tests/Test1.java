package basePackage.Tests;


import basePackage.model.ArbitraryPrecisionArithmetic.IBigInteger;
import basePackage.model.RSA.AlgorithmRSA;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

public class Test1 extends Assert{

    @Test
    public void testPrime() {
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            IBigInteger n = IBigInteger.randomBigInt(15, rnd);
            BigInteger n1 = new BigInteger(n.toString());
            assertEquals(IBigInteger.isPrime(n), n1.isProbablePrime(38));
        }
    }

    @Test
    public void testArithmeticOperations() {
        String n1 = "12345";
        String n2 = "3547";
        IBigInteger a1 = new IBigInteger(n1);
        BigInteger b1 = new BigInteger(n1);
        IBigInteger a2 = new IBigInteger(n2);
        BigInteger b2 = new BigInteger(n2);
        assertEquals(a1.mul(a2).toString(), b1.multiply(b2).toString());
        assertEquals(a1.sub(a2).toString(), b1.subtract(b2).toString());
        assertEquals(a1.div(a2).toString(), b1.divide(b2).toString());
        assertEquals(a1.add(a2).toString(), b1.add(b2).toString());
    }

    @Test
    public void testRSA() {
        AlgorithmRSA user = new AlgorithmRSA(15);
        String message = "Hello world!";
        String message2 = "Test 2";
        user.genKeys();
        String encodedStr = user.encoding(message, user.getPublicKey());
        assertEquals(message, user.decoding(encodedStr));
        user.genKeys();
        encodedStr = user.encoding(message2, user.getPublicKey());
        assertEquals(message2, user.decoding(encodedStr));
    }
}
