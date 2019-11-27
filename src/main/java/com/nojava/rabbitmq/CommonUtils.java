package com.nojava.rabbitmq;

import java.util.Random;

public class CommonUtils {

    public static void doWork(String task) throws InterruptedException {
        Thread.sleep(new Random().nextInt(1000));
    }

}
