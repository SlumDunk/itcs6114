package com.uncc;

public class FindMedian {
    public static void main(String[] args) {
        double[] a = {3};
        double[] b = {1, 2};
        System.out.println(findMedian(a, b));
    }

    public static double findMedian(double[] a, double[] b) {
        int aLen = a.length, bLen = b.length;
        double[] c = new double[aLen + bLen];
        int j = 0, k = 0;
        for (int i = 0; i < c.length; i++) {
            if (k < aLen && j < bLen && a[k] < b[j]) {//a[k] is smaller than b[j]
                c[i] = a[k];
                k++;
            } else if (k < aLen && j < bLen && a[k] > b[j]) {//a[k] is larger than b[j]
                c[i] = b[j];
                j++;
            } else if (k < aLen && j >= bLen) {// the rest of a
                c[i] = a[k];
            } else {// the rest of b
                c[i] = b[j];
            }
        }
        if (c.length % 2 == 0) {//the total num is even
            return (c[c.length / 2 - 1] + c[c.length / 2]) / 2;
        } else {// the total num is odd
            return c[c.length / 2];
        }

    }
}
