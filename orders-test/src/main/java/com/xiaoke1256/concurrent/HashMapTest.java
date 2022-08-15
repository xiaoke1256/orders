package com.xiaoke1256.concurrent;

import java.util.HashMap;
import java.util.Map;

/**
 * 注意：jdk1.7及以下环境下有一定概率发生死循环
 */
public class HashMapTest {
    static Map<String,String> map = new HashMap<String,String>();

    public static class AddThread implements Runnable {

        int start = 0;

        public AddThread(int start){
            this.start = start;
        }

        @Override
        public void run() {
            for(int i = start ; i < 100000; i+=2 ){
                map.put(Integer.toString(i), Integer.toBinaryString(i));
            }
        }

        public static void main(String[] args) throws InterruptedException {
            Thread t1 = new Thread(new AddThread(0));
            Thread t2 = new Thread(new AddThread(1));
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println(map.size());
        }
    }
}
