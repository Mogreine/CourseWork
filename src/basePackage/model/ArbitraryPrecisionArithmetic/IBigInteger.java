package basePackage.model.ArbitraryPrecisionArithmetic;

import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Класс, реализующий длинную арифметику.
 * @author Семин Кирилл
 */
public class IBigInteger implements Comparable<IBigInteger> {

    /** Основание системы счисления */
    public final static int BASE = 10;
    /** Максимально возможная длина числа */
    public final static int MAX_SIZE = (int)1e4;
    /** Константные значения единицы и нуля в длинной арифметике */
    public final static IBigInteger ONE = new IBigInteger(1L);
    public final static IBigInteger ZERO = new IBigInteger(0L);
    /** Массив цифр числа */
    private int[] numsArr;
    /** Знак числа */
    private boolean negative;
    /** Длина числа */
    private int size;

    /**
     * Создает объект на основе числа типа long
     * @param number Число для создания объекта
     */
    public IBigInteger(long number) {
        negative = number < 0;
        number = Math.abs(number);
        numsArr = new int[MAX_SIZE];
        size = 0;
        if (number == 0) {
            size++;
        }
        while (number > 0) {
            numsArr[size++] = (int) (number % BASE);
            number /= BASE;
        }
    }

    /**
     * Создает объект на основе строки
     * @param number Строка, представляющая число
     */
    public IBigInteger(String number) {
        numsArr = new int[MAX_SIZE];
        negative = number.charAt(0) == '-';
        size = number.length();
        Predicate<Integer> cycle = i -> i >= 0;
        if (negative) {
            cycle = i -> i > 0;
        }
        for (int i = size - 1, j = 0; cycle.test(i); i--, j++) {
            numsArr[j] = Integer.parseInt(number.substring(i, i + 1));
        }
        if (negative) {
            size--;
        }
    }

    /**
     * Создает объект на основе другого числа, представленного классом IBigInteger
     * @param number Экземпляр класса IBigInteger
     */
    public IBigInteger(IBigInteger number) {
        negative = number.negative;
        numsArr = new int[MAX_SIZE];
        size = number.size();
        for (int i = 0; i < number.size(); i++) {
            numsArr[i] = number.get(i);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (negative) {
            result.append('-');
        }
        for (int i = size - 1; i >= 0; i--) {
            result.append(numsArr[i]);
        }
        return result.toString();
    }

    @Override
    public int compareTo(IBigInteger number) {
        if (number.negative != this.negative) {
            return this.negative ? -1 : 1;
        }
        BiPredicate<Integer, Integer> comp = (a, b) -> a < b;
        if (this.negative) {
            comp = comp.negate();
        }
        if (number.size() != this.size()) {
            return comp.test(this.size(), number.size()) ? -1 : 1;
        }
        for (int i = size() - 1; i >= 0; i--) {
            if (this.get(i) != number.get(i)) {
                return comp.test(this.get(i), number.get(i)) ? -1 : 1;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        else {
            return this.compareTo((IBigInteger) obj) == 0;
        }
    }

    /**
     * Генерирует случайное число длинной size. Каждая цифра генерируется отдельно.
     * @param size Длина нового числа
     * @param rand Объект для генерации случайных чисел
     * @return Случайное число
     */
    public static IBigInteger randomBigInt(int size, Random rand) {
        IBigInteger result = new IBigInteger(0L);
        //Первая цифра всегда > 0
        result.set(size - 1, 1 + rand.nextInt(10 - 1));
        for (int i = 0; i < size - 1; i ++) {
            result.set(i, rand.nextInt(10));
        }
        result.size = size;
        if (result.get(0) % 2 == 0) {
            result.set(0, result.get(0) + 1);
        }
        return result;
    }

    /**
     * Гененерирует случайное число длинной size, но меньше bound
     * @param size Длина числа
     * @param rand Объект для генерации случайных чисел
     * @param bound Верхняя граница для генерируемого числа
     * @return Случайное число меньше bound
     */
    public static IBigInteger randomBigInt(int size, Random rand, IBigInteger bound) {
        IBigInteger result = new IBigInteger(0L);
        result.set(size - 1, 1 + rand.nextInt(10 - 1));
        for (int i = 0; i < size - 1; i ++) {
            result.set(i, rand.nextInt(10));
        }
        result.size = size;
        if (result.get(0) % 2 == 0) {
            result.set(0, result.get(0) + 1);
        }
        for (int i = result.size() - 1; i >= 0 && result.get(i) >= bound.get(i); i--) {
            while (result.get(i) >= bound.get(i) && result.get(i) > 0) {
                result.set(i, result.get(i) - 1);
            }
        }
        return result;
    }

    /**
     * Находит НОД двух чисел
     * @param a, b Числа у которых находится НОД
     * @return НОД двух чисел
     */
    public static IBigInteger gcd(IBigInteger a, IBigInteger b) {
        return b.compareTo(ZERO) != 0 ? gcd(b, a.mod(b)) : a;
    }

    /**
     * Обертка для расширенного алгоритма Евклида. Нужен для нахождения положительного х, если изначально он таковым не
     * получился.
     * @param a, b Числа к которым применяется алгоритм
     * @param x, y Коэффициенты, с помощью которых НОД двух чисел выражается через сами эти числа
     * @return НОД
     */
    public static IBigInteger gcdEx(IBigInteger a, IBigInteger b, IBigInteger x, IBigInteger y) {
        IBigInteger d = gcdEx1(a, b, x, y);
        //Нахождение минимального положительного x
        if (x.compareTo(ZERO) < 0) {
            IBigInteger delta = b.div(d);
            IBigInteger factor = x.div(delta);
            if (abs(x).mod(delta).compareTo(ZERO) != 0) {
                factor = factor.add(ONE);
            }
            x.change(x.add(factor.mul(delta)));
        }
        return d;
    }

    /**
     * Реализует расширенный алгоритм Евклида - помимо НОД, находит коэффициенты, через которые он выражается.
     * @param a, b Числа к которым применяется алгоритм
     * @param x, y Коэффициенты, с помощью которых НОД двух чисел выражается через сами эти числа
     * @return НОД
     */
    public static IBigInteger gcdEx1(IBigInteger a, IBigInteger b, IBigInteger x, IBigInteger y) {
        if (a.compareTo(ZERO) == 0) {
            x.change(ZERO);
            y.change(ONE);
            return b;
        }
        IBigInteger x1 = new IBigInteger(ZERO);
        IBigInteger y1 = new IBigInteger(ZERO);
        IBigInteger d = gcdEx1(b.mod(a), a, x1, y1);
        x.change(y1.sub((b.div(a)).mul(x1)));
        y.change(x1);
        return d;
    }

    /**
     * Обертка для метода isPrime. Задает количество раундов.
     * @param number Число для проверки на простоту
     * @return Простое число или нет.
     */
    public static boolean isPrime(IBigInteger number) {
        return isPrime(number, 37);
    }

    /**
     * Осуществляет проверку числа на самые просты варианты непростоты.
     * @param num Число для проверки
     * @return Очевидно число простое или нет
     */
    private static boolean obviousNotPrime(IBigInteger num) {
        return IBigInteger.isEven(num) || num.get(0) % 5 == 0;
    }

    /**
     * Осуществляет проверку на простоту используя вероятностный тест Миллера-Рабина.
     * @param number Число для проверки
     * @param k Количество раундов
     * @return Простое число или нет
     */
    private static boolean isPrime(IBigInteger number, int k) {
        //чтобы не тратить время сразу проверяем очевидные варианты, при которых число не является простым
        if (IBigInteger.obviousNotPrime(number)) {
            return false;
        }
        IBigInteger t = number.sub(ONE);
        long s = 0;
        while (IBigInteger.isEven(t)) {
            s++;
            t = t.div(2);
        }
        IBigInteger num_1 = number.sub(ONE);
        IBigInteger two = new IBigInteger(2);
        Random rand = new Random();
        //k раз проверяем число на простоту используя другое как "свидетеля"
        for (int i = 0; i < k; i++) {
            IBigInteger a = IBigInteger.randomBigInt(number.size(), rand, number.sub(two));
            IBigInteger x = IBigInteger.powMod(a, t, number);
            if (x.compareTo(ONE) == 0 || x.compareTo(num_1) == 0) {
                continue;
            }
            boolean flag = false;
            for (int ii = 0; ii < s - 1; ii++) {
                x = powMod(x, 2, number);
                if (x.compareTo(ONE) == 0) {
                    return false;
                }
                if (x.compareTo(num_1) == 0) {
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

    /**
     * Реализует бинарное возведение в степень по модулю.
     * @param n Число(представлено классом IBigInteger) для возведения в степень
     * @param pow Степень(представлено примитивным типом int)
     * @param mod Модуль(представлено классом IBigInteger)
     * @return Число n в степени pow по модулю mod
     */
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

    /**
     * Реализует бинарное возведение в степень по модулю.
     * @param n Число(представлено классом IBigInteger) для возведения в степень
     * @param pow Степень(представлено классом IBigInteger)
     * @param mod Модуль(представлено классом IBigInteger)
     * @return Число n в степени pow по модулю mod
     */
    public static IBigInteger powMod(IBigInteger n, IBigInteger pow, IBigInteger mod) {
        if (pow.compareTo(ZERO) == 0) {
            return new IBigInteger(1L);
        }
        IBigInteger tmp = powMod(n, pow.div(2), mod);
        if (pow.mod(2) == 0) {
            return (tmp.mul(tmp)).mod(mod);
        }
        else {
            return (n.mul(tmp.mul(tmp))).mod(mod);
        }
    }

    /**
     * Находит модуль числа.
     * @param number Число, для которого находится модуль
     * @return Модуль числа
     */
    public static IBigInteger abs(IBigInteger number) {
        IBigInteger result = new IBigInteger(number);
        result.negative = false;
        return result;
    }

    /**
     * Определяет четное ли число.
     * @param number Число для определения четности
     * @return Четное ли число
     */
    public static boolean isEven(IBigInteger number) {
        return number.get(0) % 2 == 0;
    }

    /**
     * @return Возвращает длину числа
     */
    public int size() {
        return size;
    }

    /**
     * Возвращает цифру по ее индексу.
     * @param index index of number need to change
     * @return Значение цифры по индексу
     */
    public int get(int index) {
        return numsArr[index];
    }

    /**
     * Change number by index.
     * @param index index
     * @param value Новое значение
     */
    public void set(int index, int value) {
        numsArr[index] = value;
    }

    /**
     * Copy all properties of gotten number.
     * @param number Объект, свойства которого копируются
     */
    public void change(IBigInteger number) {
        for (int i = 0; i < size; i++) {
            numsArr[i] = 0;
        }
        this.negative = number.negative;
        size = number.size();
        for (int i = 0; i < number.size(); i++) {
            numsArr[i] = number.get(i);
        }
    }

    /**
     * Осуществляет сложение двух чисел
     * @param number 2 слагаемое
     * @return Результат сложения двух чисел
     */
    public IBigInteger add(IBigInteger number) {
        //следующие два блока if нужны для работы со знаками чисел
        if (!this.negative && number.negative) {
            IBigInteger tmp = new IBigInteger(number);
            tmp.negative = false;
            return this.sub(tmp);
        }
        if (this.negative && !number.negative) {
            IBigInteger tmp = new IBigInteger(this);
            tmp.negative = false;
            return number.sub(tmp);
        }
        IBigInteger result = new IBigInteger(0L);
        int carry = 0;
        int forSize = Math.max(this.size(), number.size());
        for (int i = 0; i < forSize || carry != 0; i++) {
            long x = (long) this.get(i) + number.get(i) + carry;
            result.set(i, (int) x % BASE);
            carry = (int) x / 10;
        }
        result.size = Math.max(this.size(), number.size()) + 1;
        while (result.size > 1 && result.get(result.size - 1) == 0) {
            result.size--;
        }
        if (number.negative && this.negative) {
            result.negative = true;
        }
        return result;
    }

    /**
     * Осуществляет вычитание между объектом и другим числом типа IBigInteger
     * @param number Вычитаемое число
     * @return Результат операции вычитания
     */
    public IBigInteger sub(IBigInteger number) {
        //следующие 3 блока if обрабатываеют все случаю с возможными знаками чисел
        if (this.negative && number.negative) {
            IBigInteger tmp = new IBigInteger(number);
            tmp.negative = false;
            return tmp.add(this);
        }
        if (number.negative || this.negative) {
            IBigInteger tmp = new IBigInteger(number);
            tmp.negative = !number.negative;
            return this.add(tmp);
        }
        if (this.compareTo(number) < 0) {
            IBigInteger result = number.sub(this);
            result.negative = true;
            return result;
        }
        IBigInteger result = new IBigInteger(0L);
        int carry = 0;
        for (int i = 0; i < this.size() || carry != 0; i++) {
            long x = (long) this.get(i) - number.get(i) + carry;
            carry = x < 0 ? -1 : 0;
            result.set(i, (int) (x + BASE) % BASE);
        }
        result.size = this.size();
        while (result.size > 1 && result.get(result.size - 1) == 0) {
            result.size--;
        }
        return result;
    }

    /**
     * Производит умножение двух чисел
     * @param number 2 множитель
     * @return Произведение объекта и другого числа
     */
    public IBigInteger mul(IBigInteger number) {
        IBigInteger result = new IBigInteger(0L);
        boolean neg1 = this.negative;
        boolean neg2 = number.negative;
        // Определение знака результата
        if (number.negative && this.negative) {
            result.negative = false;
        } else if (number.negative || this.negative) {
            result.negative = true;
        }
        this.negative = false;
        number.negative = false;
        for (int i = 0; i < this.size(); i++) {
            int carry = 0;
            for (int j = 0; j < number.size() || carry != 0; j++) {
                long x = result.get(i + j) + (long) this.get(i) * number.get(j) + carry;
                result.set(i + j, (int) x % BASE);
                carry = (int) x / BASE;
            }
        }
        result.size = this.size() + number.size();
        while (result.size > 1 && result.get(result.size - 1) == 0) {
            result.size--;
        }
        this.negative = neg1;
        number.negative = neg2;
        // Если 0 - знак должен быть положительным
        if (IBigInteger.abs(result).compareTo(ZERO) == 0) {
            result.negative = false;
        }
        return result;
    }

    /**
     * Производит умножение двух чисел, где 2 типа int
     * @param number 2 множитель
     * @return Произведение объекта и другого числа
     */
    public IBigInteger mul(int number) {
        IBigInteger result = new IBigInteger(0L);
        boolean neg1 = this.negative;
        if (number < 0 && this.negative) {
            result.negative = false;
        }
        else if (number < 0 || this.negative) {
            result.negative = true;
        }
        this.negative = false;
        number = Math.abs(number);
        int carry = 0;
        for (int i = 0; i < this.size() || carry != 0; i++) {
            long x = (long) this.get(i) * number + carry;
            result.set(i, (int) (x % BASE));
            carry = (int) x / BASE;
        }
        result.size = this.size() + 1;
        while (result.size > 1 && result.get(result.size - 1) == 0) {
            result.size--;
        }
        this.negative = neg1;
        if (IBigInteger.abs(result).compareTo(ZERO) == 0) {
            result.negative = false;
        }
        return result;
    }

    /**
     * Производит деление объекта на число
     * @param number Делитель
     * @return Частное от деления объекта на другое число
     */
    public IBigInteger div(IBigInteger number) {
        IBigInteger result = new IBigInteger(0L);
        boolean neg1 = this.negative;
        boolean neg2 = number.negative;
        // Определение знака числа
        if (number.negative && this.negative) {
            result.negative = false;
        }
        else if (number.negative || this.negative) {
            result.negative = true;
        }
        this.negative = false;
        number.negative = false;
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
        this.negative = neg1;
        number.negative = neg2;
        if (IBigInteger.abs(result).compareTo(ZERO) == 0) {
            result.negative = false;
        }
        return result;
    }

    /**
     * Производит деление объекта на число типа int
     * @param number Делитель
     * @return Частное от деления объекта на другое число
     */
    public IBigInteger div(int number) {
        IBigInteger result = new IBigInteger(0L);
        boolean neg1 = this.negative;
        if (number < 0 && this.negative) {
            result.negative = false;
        }
        else if (number < 0 || this.negative) {
            result.negative = true;
        }
        this.negative = false;
        number = Math.abs(number);
        int carry = 0;
        for (int i = this.size() - 1; i >= 0; i--) {
            long x = (long) carry * BASE + this.get(i);
            result.set(i, (int) x / number);
            carry = (int) x % number;
        }
        result.size = this.size();
        while (result.size > 1 && result.get(result.size - 1) == 0) {
            result.size--;
        }
        this.negative = neg1;
        if (IBigInteger.abs(result).compareTo(ZERO) == 0) {
            result.negative = false;
        }
        return result;
    }

    /**
     * Находит остаток от деления
     * @param number Число на которое делится объект
     * @return Остаток от деления на number
     */
    public IBigInteger mod(IBigInteger number) {
        boolean neg1 = this.negative;
        boolean neg2 = number.negative;
        this.negative = number.negative = false;
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
            int digit = number.mul(r).compareTo(carry) <= 0 ? r : l;
            carry = carry.sub(number.mul(digit));
        }
        this.negative = neg1;
        number.negative = neg2;
        carry.negative = this.negative;
        return carry;
    }

    /**
     * Находит остаток от деления на число типа int
     * @param number Число на которое делится объект
     * @return Остаток от деления на number
     */
    public int mod(int number) {
        int carry = 0;
        boolean neg1 = this.negative;
        this.negative = false;
        for (int i = this.size() - 1; i >= 0; i--) {
            carry = (int) (((long) carry * BASE + this.get(i)) % number);
        }
        if (this.negative) {
            carry *= -1;
        }
        this.negative = neg1;
        return carry;
    }

}