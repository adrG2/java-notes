package com.github.staticDirective.lib;

public class SingletonTest {

    private static SingletonTest instance = null;
    private int i;

    private SingletonTest(int i) {
        this.i = i;
    }

    public static SingletonTest getInstance(int i) {
        if (instance == null) {
            instance = new SingletonTest(i);
        }

        return instance;
    }

    public int getI() {
        return this.i;
    }

    public void setI(int i) {
        this.i = i;
    }

}