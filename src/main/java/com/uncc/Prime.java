package com.uncc;

public class Prime {
    public static void main(String[] args) {
        int count = 0;
        for (int i = 2; i < 15; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        System.out.println(count);
    }

    /**
     * a method to determine whether the num is a prime num
     *
     * @param n
     * @return
     */
    public static boolean isPrime(long n) {
        for (int j = 2; j <= Math.sqrt(n); j++) {
            if (n % j == 0) {
                return false;
            }
        }
        return true;
    }
}
