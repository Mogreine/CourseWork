package basePackage.model.RSA;

import basePackage.model.ArbitraryPrecisionArithmetic.IBigInteger;

import java.util.Random;
import java.util.StringTokenizer;

/**
 * Класс, реализующий работу алгоритма RSA.
 * Содержит методы для генерации простых чисесл, шифрования и дешифрования строк.
 * @author Семин Кирилл
 */
public class AlgorithmRSA {

    /**
     * Статичный класс предоставляющий пару чисел типа IBigInteger
     */
    public static class KeysPair {
        IBigInteger n1;
        IBigInteger n2;

        /**
         * Конструктор - без параметров, создает 2 числа равных 0
         */
        public KeysPair() {
            this.n1 = new IBigInteger(0L);
            this.n2 = new IBigInteger(0L);
        }

        /**
         * Конструктор - создает объект на основе двух других чисел
         */
        public KeysPair(IBigInteger n1, IBigInteger n2) {
            this.n1 = new IBigInteger(n1);
            this.n2 = new IBigInteger(n2);
        }
    }

    /** Одно из больших простых чисел в двоичном представлении которого всего 2 единицы */
    private static final IBigInteger e = new IBigInteger(65537L);
    /** Открытые и закрытые ключи */
    private KeysPair publicKey;
    private KeysPair privateKey;
    /** Длина зашифрованных чисел */
    private int keySize;
    /** Пара простых чисел для генерации */
    private KeysPair primeNumbers;

    /**
     * Конструктор - задает длину зашифрованного числа
     * @param keySize Длина
     */
    public AlgorithmRSA(int keySize) {
        this.keySize = keySize;
        primeNumbers = null;
    }

    /**
     * Шифрует сообщение посимвольно
     * @param message Сообщение
     * @param anotherOpenKey открытый ключ получателя
     * @return Зашифрованное сообщение
     */
    public String encoding(String message, KeysPair anotherOpenKey) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '\n') {
                continue;
            }
            result.append(IBigInteger.powMod(new IBigInteger(message.charAt(i)), anotherOpenKey.n1, anotherOpenKey.n2).toString()).append(" ");
        }
        return result.toString();
    }

    /**
     * Расшифровывает закодированное сообщение
     * @param message Сообщение
     * @return Расшифрованное сообщение
     */
    public String decoding(String message) {
        StringBuilder result = new StringBuilder();
        StringTokenizer mm = new StringTokenizer(message, " ");
        while (mm.hasMoreTokens()) {
            String temp = mm.nextToken();
            if (temp.charAt(temp.length() - 1) == '\n') {
                temp = temp.substring(0, temp.length() - 1);
            }
            if (temp.equals("\n") || temp.equals("")) {
                result.append("\n");
                continue;
            }
            result.append((char) Integer.parseInt(IBigInteger.powMod(new IBigInteger(temp), privateKey.n1, privateKey.n2).toString()));
        }
        return result.toString();
    }

    /**
     * Генерирует 2 простых числа определеннной длины. Для проверки на простоту использует тест Миллера-Рабина
     * @param length длина генерируемых чисел
     * @return Пару сгенерированных чисел
     */
    public KeysPair genPrimeNumbers(int length) {
        Random rand = new Random();
        KeysPair keys = new KeysPair(IBigInteger.randomBigInt(length, rand), IBigInteger.randomBigInt(length, rand));
        while (!IBigInteger.isPrime(keys.n1)) {
            keys.n1 = IBigInteger.randomBigInt(length, rand);
        }
        while (!IBigInteger.isPrime(keys.n2)) {
            keys.n2 = IBigInteger.randomBigInt(length, rand);
        }
        return keys;
    }

    /**
     * Генерирует закрытый и открытый ключи
     */
    public void genKeys() {
        primeNumbers = genPrimeNumbers(keySize);
        IBigInteger n = primeNumbers.n2.mul(primeNumbers.n1);
        genPublicKey(n);
        genPrivateKey(n);
    }

    /**
     * Определяет сгенерированы ли ключи
     * @return Сгенерированы ли ключи
     */
    public boolean areKeysGenerated() {
        return primeNumbers != null;
    }

    /**
     * Генерирует открытый ключ
     * @param n Произведение двух простых чисел
     */
    public void genPublicKey(IBigInteger n) {
        publicKey = new KeysPair(e, n);
    }

    /**
     * Генерирует закрытый ключ
     * @param n Произведение двух простых чисел
     */
    public void genPrivateKey(IBigInteger n) {
        IBigInteger fi = primeNumbers.n1.sub(IBigInteger.ONE).mul(primeNumbers.n2.sub(IBigInteger.ONE));
        privateKey = new KeysPair(calcD(fi), n);
    }

    /**
     * Расчитывает секретную экспоненту
     * @param fi Значение функции Эйлера от числа n
     * @return Секретную экспоненту
     */
    private IBigInteger calcD(IBigInteger fi) {
        IBigInteger d = new IBigInteger(IBigInteger.ZERO);
        IBigInteger.gcdEx(e, fi, d, new IBigInteger(IBigInteger.ZERO));
        d = d.mod(fi);
        return d;
    }

    public KeysPair getPublicKey() {
        return publicKey;
    }

    public KeysPair getPrivateKey() {
        return privateKey;
    }
}
