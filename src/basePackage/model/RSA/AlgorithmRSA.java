package basePackage.model.RSA;

import basePackage.model.ArbitraryPrecisionArithmetic.IBigInteger;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class AlgorithmRSA {

    private static final IBigInteger e = new IBigInteger(65537L);

    private Keys.KeysPair publicKey;
    private Keys.KeysPair privateKey;
    public int keySize;
    private Keys.KeysPair keysPair = null;
    private IBigInteger n;

    public AlgorithmRSA(int keySize) {
        this.keySize = keySize;
    }

    public String encoding(String message, Keys.KeysPair anotherOpenKey) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '\n') {
                continue;
            }
            result.append(IBigInteger.powMod(new IBigInteger(message.charAt(i)), anotherOpenKey.n1, anotherOpenKey.n2).toString()).append(" ");
        }
        return result.toString();
    }

    public String decoding(String message) {
        StringBuilder result = new StringBuilder();
        StringTokenizer mm = new StringTokenizer(message, " ");
        while (mm.hasMoreTokens()) {
            String temp = mm.nextToken();
            if (temp.charAt(temp.length() - 1) == '\n') {
                temp = temp.substring(0, temp.length() - 1);
            }
            if (temp.equals("\n") || temp.equals("")) {
                result.append("\n");
                continue;
            }
            result.append((char) Integer.parseInt(IBigInteger.powMod(new IBigInteger(temp), privateKey.n1, privateKey.n2).toString()));
        }
        return result.toString();
    }

    public void stopGeneration() {
        Keys.stopGen();
    }

    public boolean isGenerating() {
        return Keys.isGenerating();
    }

    public boolean genKeys() throws InterruptedException {
        if (keysPair == null || keysPair.n1.equals(IBigInteger.ZERO)) {
            keysPair = Keys.genPrimeNumbers(keySize);
            if (keysPair.n1.equals(IBigInteger.ZERO) || keysPair.n2.equals(IBigInteger.ZERO)) {
                return false;
            }
            n = keysPair.n2.mul(keysPair.n1);
        }
        else {
            return false;
        }
        genPublicKey();
        genPrivateKey();
        return true;
    }

    public boolean areKeysGenerated() {
        return !isGenerating() && (keysPair != null && (keysPair.n1 != null && keysPair.n2 != null) && !keysPair.n1.equals(IBigInteger.ZERO) && !keysPair.n2.equals(IBigInteger.ZERO));
    }

    public void genPublicKey() {
        publicKey = new Keys.KeysPair(e, n);
    }

    public void genPrivateKey() {
        IBigInteger fi = keysPair.n1.sub(IBigInteger.ONE).mul(keysPair.n2.sub(IBigInteger.ONE));
        privateKey = new Keys.KeysPair(calcD(fi), n);
    }

    private IBigInteger calcD(IBigInteger fi) {
        IBigInteger d = new IBigInteger(IBigInteger.ZERO);
        IBigInteger.gcdEx(e, fi, d, new IBigInteger(IBigInteger.ZERO));
        d = d.mod(fi);
        return d;
    }

    public Keys.KeysPair getPublicKey() {
        return publicKey;
    }

    public Keys.KeysPair getPrivateKey() {
        return privateKey;
    }
}
