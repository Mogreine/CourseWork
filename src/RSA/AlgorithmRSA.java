package RSA;

import ArbitraryPrecisionArithmetic.IBigInteger;

public class AlgorithmRSA {

    private static final IBigInteger e = new IBigInteger(65537L);

    private Keys.KeysPair publicKey;
    private Keys.KeysPair privateKey;

    public AlgorithmRSA(int keysSize) {
        genKeys(keysSize);
    }

    public String encoding(IBigInteger message, Keys.KeysPair anotherOpenKey) {
        return IBigInteger.powMod(message, anotherOpenKey.n1, anotherOpenKey.n2).toString();
    }

    public String decoding(IBigInteger message) {
        return IBigInteger.powMod(message, privateKey.n1, privateKey.n2).toString();
    }

    private void genKeys(int keySize) {
        Keys.KeysPair keysPair = Keys.genPrimeNumbers(keySize);
        IBigInteger n = keysPair.n2.mul(keysPair.n1);
        IBigInteger fi = keysPair.n1.sub(IBigInteger.ONE).mul(keysPair.n2.sub(IBigInteger.ONE));
        publicKey = new Keys.KeysPair(e, n);
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
