package basePackage;

import basePackage.model.ArbitraryPrecisionArithmetic.IBigInteger;
import basePackage.model.RSA.AlgorithmRSA;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/*
public class TestPrimeGenerate {
    public static void main(String[] args) {
        int size = 200;
        Random rand = new Random();
        long[] results = new long[size];
        IBigInteger n;
        int z = 0;
        double ss = 9999;
        for (int i = 20; i < 150; i++, z++) {
            ss *= 10;
            n = IBigInteger.randomBigInt(i, rand);
            long t1 = System.currentTimeMillis();
            int k = (int)Math.log(ss);
            IBigInteger.isPrime(n, k);
            long t2 = System.currentTimeMillis();
            results[z] = t2 - t1;
        }
        try (FileWriter out = new FileWriter(new File("src/basePackage/test2.txt"), false)) {
            for (int i = 0; i <= z; i++) {
                out.write(String.format("%d\r\n", results[i]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/
