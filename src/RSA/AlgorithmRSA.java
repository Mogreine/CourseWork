package RSA;

import ArbitraryPrecisionArithmetic.IBigInteger;

public class AlgorithmRSA {

    private static final IBigInteger e = new IBigInteger(65537L);

    private Keys.KeysPair openKey;
    private Keys.KeysPair closeKey;

    public AlgorithmRSA() {
        genKeys();
    }

    public String encoding(IBigInteger message, Keys.KeysPair anotherOpenKey) {
        return IBigInteger.powMod(message, anotherOpenKey.n1, anotherOpenKey.n2).toString();
    }

    public String decoding(IBigInteger message) {
        return IBigInteger.powMod(message, closeKey.n1, closeKey.n2).toString();
    }

    private void genKeys() {
        Keys.KeysPair keysPair = Keys.genPrimeNumbers(10);
        IBigInteger n = keysPair.n2.mul(keysPair.n1);
        IBigInteger fi = keysPair.n1.sub(IBigInteger.ONE).mul(keysPair.n2.sub(IBigInteger.ONE));
        openKey = new Keys.KeysPair(e, n);
        closeKey = new Keys.KeysPair(calcD(fi), n);
    }

    private IBigInteger calcD(IBigInteger fi) {
        IBigInteger d = new IBigInteger(IBigInteger.ZERO);
        IBigInteger.gcdEx(e, fi, d, new IBigInteger(IBigInteger.ZERO));
        d = (d.mod(fi).add(fi)).mod(fi);
        return d;
    }

    public Keys.KeysPair getOpenKey() {
        return openKey;
    }

    public Keys.KeysPair getCloseKey() {
        return closeKey;
    }
}
