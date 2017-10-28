package ArbitraryPrecisionArithmetic;

import java.util.Arrays;

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


}
