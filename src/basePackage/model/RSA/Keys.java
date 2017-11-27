package basePackage.model.RSA;

import basePackage.model.ArbitraryPrecisionArithmetic.IBigInteger;

public class Keys {

    public static class KeysPair {
        IBigInteger n1;
        IBigInteger n2;

        public KeysPair() {
            this.n1 = new IBigInteger(0L);
            this.n2 = new IBigInteger(0L);
        }

        public KeysPair(IBigInteger n1, IBigInteger n2) {
            this.n1 = new IBigInteger(n1);
            this.n2 = new IBigInteger(n2);
        }

        public IBigInteger getN1() {
            return n1;
        }

        public IBigInteger getN2() {
            return n2;
        }
    }

    private static class IThread extends Thread {
        private IBigInteger number;

        public IThread(IBigInteger number) {
            this.number = number;
        }

        @Override
        public void run() {
            while (!IBigInteger.isPrime(number)) {
                if (!Thread.interrupted()) {
                    number.change(IBigInteger.randomBigInt(number.size()));
                }
                else {
                    number.change(new IBigInteger(IBigInteger.ZERO));
                    return;
                }
            }
        }
    }

    public static IThread n1;
    public static IThread n2;


    static public KeysPair genPrimeNumbers(int length) {
        KeysPair keys = new KeysPair(IBigInteger.randomBigInt(length), IBigInteger.randomBigInt(length));
        n1 = new IThread(keys.n1);
        n2 = new IThread(keys.n2);
        n1.start();
        n2.start();
        try {
            n1.join();
            n2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return keys;
    }

    public static void stopGen() {
        n1.interrupt();
        n2.interrupt();
    }

    public static boolean isGenerating() {
        return (n1 != null && n2 != null) && (n1.isAlive() || n2.isAlive());
    }
}
