package com.thoughtworks.gauge.gradle.sample;

public class Main {
    public static void main(String[] args) {
        Test test = new Test("test_value");
        System.out.println("com.thoughtworks.gauge.gradle.sample.Test Value is : " + test.getValue());
    }
}
