package basePackage.model.RSA;

import basePackage.model.ArbitraryPrecisionArithmetic.IBigInteger;

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
            result.append(IBigInteger.powMod(new IBigInteger(message.charAt(i)), anotherOpenKey.n1, anotherOpenKey.n2).toString());
        }
        return result.toString();
    }

    public String decoding(String message) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < message.length(); i += keySize) {
            if (message.charAt(i) == '\n') {
                continue;
            }
            result.append(IBigInteger.powMod(new IBigInteger(message.substring(i, i + keySize)), privateKey.n1, privateKey.n2).toString());
        }
        return result.toString();
    }

    public void genKeys() {
        genPublicKey();
        genPrivateKey();
    }

    public void genPublicKey() {
        if (keysPair == null) {
            keysPair = Keys.genPrimeNumbers(keySize);
            n = keysPair.n2.mul(keysPair.n1);
        }
        publicKey = new Keys.KeysPair(e, n);
    }

    public void genPrivateKey() {
        if (keysPair == null) {
            keysPair = Keys.genPrimeNumbers(keySize);
            n = keysPair.n2.mul(keysPair.n1);
        }
        IBigInteger fi = keysPair.n1.sub(IBigInteger.ONE).mul(keysPair.n2.sub(IBigInteger.ONE));
        privateKey = new Keys.KeysPair(calcD(fi), n);
    }

    private IBigInteger calcD(IBigInteger fi) {
        IBigInteger d = new IBigInteger(IBigInteger.ZERO);
        IBigInteger.gcdEx1(e, fi, d, new IBigInteger(IBigInteger.ZERO));
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
