package com.fs.mongo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by fangsheng on 2017/8/27.
 *
 * @cnstonefang@gmail.com
 */
public class Application {

    public  static  void main (String[] args) {
       ThreadUtil threadUtil = ThreadUtil.newInstance();
       final CountDownLatch count = new CountDownLatch(10);
       for ( int i =0;i <10; i++) {
           threadUtil.addExecuteTask(new Runnable() {
               public void run() {
                   System.out.println(Thread.currentThread().getName());
                   count.countDown();
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {e.printStackTrace();

                   }
               }
           });
       }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("there is no task");
        threadUtil.shutdown();
        System.out.println("end1");
        threadUtil.prepare();
        final CountDownLatch count2 = new CountDownLatch(10);
        for ( int i =0;i <10; i++) {
            threadUtil.addExecuteTask(new Runnable() {
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    count2.countDown();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {e.printStackTrace();

                    }
                }
            });
        }
        try {
            count2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadUtil.shutdown();
        System.out.println("end2");
    }
}
