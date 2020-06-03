package com.caircb.rcbtracegadere.generics;

/**
 * Created by jlsuarez on 8/1/2018.
 */

public class MyThread {
    private MyThread() {
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
