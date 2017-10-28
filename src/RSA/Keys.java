package RSA;

import java.util.Random;

public class Keys {

    private class pair {
        Long publicKey;
        Long privateKey;

        public pair() {
            this(0L, 0L);
        }

        public pair(Long publicKey, Long privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }

    private pair keys;

//    public pair genKeys() {
//        Random randomNum = new Random();
//        long p = randomNum
//        return keys;
//    }

    private boolean isSimple(int num, int k) {
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
    }
}
