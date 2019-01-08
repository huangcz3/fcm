package com.asiainfo.fcm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by LiuJH on 2018/6/24.
 */
public class Test1 {

    public static void main(String[] args) throws InterruptedException {

          /*  Map map = new HashMap();

            map.put(null,123);
            System.out.println(map);

            ConcurrentLinkedQueue linkedQueue = new ConcurrentLinkedQueue();

            for (int i = 0; i < 2; i++) {

                //Thread.sleep(500);
                new Thread(() -> {

                    for (int j = 0; ; j++) {
                        System.out.println(linkedQueue.poll());
                    }

                }).start();
            }



            for (int i = 0; i < 5; i++) {
                new Thread(()->{
                    while (true) {
                        linkedQueue.offer("1");
                    }
                }).start();
            }
*/

        System.out.println(Integer.toString(~0010101,2));
        System.out.println(Integer.toString(0010101,2));
        System.out.println(~010);
        System.out.println(010);


    }
}
