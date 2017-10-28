package RSA;

public class AlgorithmRSA {
    private final int e = 257;
    private String text;
    private long    privateKey,
                    publicKey,
                    anotherPublicKey;

    public AlgorithmRSA (String text) {
        this.text = text;
    }

}
