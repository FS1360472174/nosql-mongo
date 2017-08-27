package com.fs.mongo;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by fangsheng on 2017/8/27.
 *
 * @cnstonefang@gmail.com
 */
public class Application {

    public  static  void main (String[] args) {
       ThreadUtil threadUtil = ThreadUtil.newInstance();

       for ( int i =0;i <10; i++) {
           threadUtil.addExecuteTask(new Runnable() {
               public void run() {
                   System.out.println(Thread.currentThread().getName());
               }
           });
           if(threadUtil.isTaskEnd()) {
               System.out.println("there is no task");
           }
       }
       threadUtil.shutdown();
    }
}
