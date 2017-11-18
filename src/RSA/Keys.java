package RSA;

import ArbitraryPrecisionArithmetic.IBigInteger;

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

        public void start() {
            run();
        }

        public void run() {
            while (!IBigInteger.isPrime(number)) {
                number.change(IBigInteger.randomBigInt(number.size()));
            }
        }
    }

    static public KeysPair genPrimeNumbers(int length) {
        KeysPair keys = new KeysPair(IBigInteger.randomBigInt(length), IBigInteger.randomBigInt(length));
        IThread n1 = new IThread(keys.n2);
        IThread n2 = new IThread(keys.n1);
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


    /*private boolean isPrime(int num, int k) {
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
