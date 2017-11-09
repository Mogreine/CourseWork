package RSA;

import ArbitraryPrecisionArithmetic.IBigInteger;
import sun.java2d.loops.GraphicsPrimitive;

public class Keys {

    public static class KeysPair {
        IBigInteger publicKey;
        IBigInteger privateKey;

        public KeysPair() {
            this.publicKey = new IBigInteger(0L);
            this.privateKey = new IBigInteger(0L);
        }

        public KeysPair(IBigInteger publicKey, IBigInteger privateKey) {
            this.publicKey = new IBigInteger(publicKey);
            this.privateKey = new IBigInteger(privateKey);
        }

        public IBigInteger getPublicKey() {
            return publicKey;
        }

        public IBigInteger getPrivateKey() {
            return privateKey;
        }
    }

    private static class IThread extends Thread {
        private IBigInteger number;

        public IThread(IBigInteger number) {
            this.number = number;
        }

        public void start() {
            run();
        }

        public void run() {
            while (!IBigInteger.isSimple(number)) {
                number.change(number.add(IBigInteger.ONE));
            }
        }
    }

    static public KeysPair genKeys(int length) {
        KeysPair keys = new KeysPair(IBigInteger.randomBigInt(length), IBigInteger.randomBigInt(length));
        IThread n1 = new IThread(keys.privateKey);
        IThread n2 = new IThread(keys.publicKey);
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


    /*private boolean isSimple(int num, int k) {
        long t = num - 1;
        long s = 0 ;
        Random randNum = new Random();
        while (t % 2 == 0) {
            s++;
            t >>= 1;
        }
        for (int i = 0; i < k; i++) {
            long a = 1 + randNum.nextInt(num - 1);
            long x = powMod(a, t, num);
            if (x == 1 || x == num - 1) {
                continue;
            }
            boolean flag = false;
            for (int ii = 0; ii < s - 1; ii++) {
                x = powMod(x, 2, num);
                if (x == 1) {
                    return false;
                }
                if (x == num - 1) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    private long fastPow(long num, long pow) {
        long result = 1;
        while (pow > 0) {
            if (pow % 2 == 1) {
                result *= num;
            }
            num *= num;
            pow >>= 1;
        }
        return result;
    }

    private long powMod(long n, long pow, long mod) {
        if (pow == 0)
            return 1;
        long tmp = powMod(n, pow / 2, mod);
        if (pow % 2 == 0) {
            return (tmp * tmp) % mod;
        }
        else {
            return (n * tmp * tmp) % mod;
        }
    }

    private long gcd (long a, long b) {
        return b != 0 ? gcd(b, a % b) : a;
    }*/
}
