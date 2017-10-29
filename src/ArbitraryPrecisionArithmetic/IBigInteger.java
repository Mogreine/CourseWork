package ArbitraryPrecisionArithmetic;

import java.util.Arrays;
import java.util.Random;

public class IBigInteger implements Comparable<IBigInteger> {

    public final static int BASE = 10;
    public final static int MAX_SIZE = (int)1e4;
    private int[] numsArr;
    protected int size;

    public IBigInteger(long number) {
        numsArr = new int[MAX_SIZE];
        Arrays.fill(numsArr, 0);
        size = 0;
        if (number == 0) {
            size++;
        }
        while (number > 0) {
            numsArr[size++] = (int) (number % BASE);
            number /= BASE;
        }
    }

    public static IBigInteger randomBigInt(int size) {
        Random rand = new Random();
        IBigInteger result = new IBigInteger(0L);
        result.set(size - 1, 1 + rand.nextInt(10 - 1));
        for (int i = 0; i < size - 1; i ++) {
            result.set(i, rand.nextInt(10));
        }
        result.size = size;
        return result;
    }

    /*public static boolean isSimple(IBigInteger number) {
        return isSimple(number, 55);
    }*/

    /*private static boolean isSimple(IBigInteger number, int k) {
        IBigInteger t = number.sub(new IBigInteger(1L));
        long s = 0;

    }

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
    }*/

    public static IBigInteger powMod(IBigInteger n, int pow, IBigInteger mod) {
        if (pow == 0) {
            return new IBigInteger(1L);
        }
        IBigInteger tmp = powMod(n, pow / 2, mod);
        if (pow % 2 == 0) {
            return (tmp.mul(tmp)).mod(mod);
        }
        else {
            return (n.mul(tmp.mul(tmp))).mod(mod);
        }
    }

    @Override
    public int compareTo(IBigInteger number) {
        if (number.size() != this.size()) {
            return this.size() < number.size() ? -1 : 1;
        }
        for (int i = size() - 1; i >= 0; i--) {
            if (this.get(i) != number.get(i)) {
                return this.get(i) < number.get(i) ? -1 : 1;
            }
        }
        return 0;
    }

    public int size() {
        return size;
    }

    public int get(int index) {
        return numsArr[index];
    }

    public void set(int index, int value) {
        numsArr[index] = value;
    }

    public IBigInteger add(IBigInteger number) {
        IBigInteger result = new IBigInteger(0L);
        int carry = 0;
        for (int i = 0; i < Math.max(this.size(), number.size()) || carry != 0; i++) {
            long x = (long) this.get(i) + number.get(i) + carry;
            this.set(i, (int) x % BASE);
            carry = (int) x / 10;
        }
        result.size = Math.max(this.size(), number.size()) + 1;
        while (result.size > 1 && result.get(result.size - 1) == 0) {
            result.size--;
        }
        return result;
    }

    /*
    Работает корректно только если number <= this
     */
    public IBigInteger sub(IBigInteger number) {
        IBigInteger result = new IBigInteger(0L);
        int carry = 0;
        for (int i = 0; i < this.size() || carry != 0; i++) {
            long x = (long) this.get(i) - number.get(i) + carry;
            carry = x < 0 ? -1 : 0;
            result.set(i, (int) (x + BASE) % BASE);
        }
        result.size = this.size();
        while (size > 1 && result.get(result.size - 1) == 0) {
            result.size--;
        }
        return result;
    }

    public IBigInteger mul(IBigInteger number) {
        IBigInteger result = new IBigInteger(0L);
        for (int i = 0; i < this.size(); i++) {
            int carry = 0;
            for (int j = 0; j < number.size() || carry != 0; j++) {
                long x = result.get(i + j) + (long) number.get(i) * number.get(j) + carry;
                result.set(i + j, (int) x % BASE);
                carry = (int) x / BASE;
            }
        }
        result.size = this.size() + number.size();
        while (result.size > 1 && result.get(result.size - 1) == 0) {
            result.size--;
        }
        return result;
    }

    public IBigInteger mul(int number) {
        IBigInteger result = new IBigInteger(0L);
        int carry = 0;
        for (int i = 0; i < this.size() || carry != 0; i++) {
            long x = (long) this.get(i) * number + carry;
            this.set(i, (int) (x % BASE));
            carry = (int) x / BASE;
        }
        result.size = this.size() + 1;
        while (result.size > 1 && result.get(result.size - 1) == 0) {
            result.size--;
        }
        return result;
    }

    public IBigInteger div(IBigInteger number) {
        IBigInteger result = new IBigInteger(0L);
        IBigInteger carry = new IBigInteger(0L);
        for (int i = this.size() - 1; i >= 0; i--) {
            carry = carry.mul(new IBigInteger(BASE));
            carry.set(0, this.get(i));
            int     l = 0,
                    r = BASE - 1,
                    m = 0;
            while (l + 1 < r) {
                m = l + (r - l) / 2;
                if (number.mul(m).compareTo(carry) <= 0) {
                    l = m;
                }
                else {
                    r = m;
                }
            }
            result.set(i, number.mul(r).compareTo(carry) <= 0 ? r : l);
            carry = carry.sub(number.mul(result.get(i)));
        }
        result.size = this.size();
        while (result.size > 1 && result.get(result.size - 1) == 0) {
            result.size--;
        }
        return result;
    }

    public IBigInteger div(int number) {
        IBigInteger result = new IBigInteger(0L);
        int carry = 0;
        for (int i = this.size() - 1; i >= 0; i--) {
            long x = (long) carry * BASE + this.get(i);
            result.set(i, (int) x / number);
            carry = carry % number;
        }
        result.size = this.size();
        while (result.size > 1 && result.get(result.size - 1) == 0) {
            result.size--;
        }
        return result;
    }

    public IBigInteger mod(IBigInteger number) {
        IBigInteger carry = new IBigInteger(0L);
        for (int i = this.size() - 1; i >= 0; i--) {
            carry = carry.mul(new IBigInteger(BASE));
            carry.set(0, this.get(i));
            int     l = 0,
                    r = BASE - 1,
                    m = 0;
            while (l + 1 < r) {
                if (number.mul(m).compareTo(carry) <= 0) {
                    l = m;
                }
                else {
                    r = m;
                }
            }
            int digit = number.mul(r).compareTo(carry) <= 0 ? r : l;
            carry = carry.sub(number.mul(digit));
        }
        return carry;
    }

    public int mod(int number) {
        int carry = 0;
        for (int i = this.size() - 1; i >= 0; i--) {
            carry = (int) (((long) carry * BASE + this.get(i)) % number);
        }
        return carry;
    }

}
