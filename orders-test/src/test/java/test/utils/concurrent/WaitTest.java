package test.utils.concurrent;

public class WaitTest {

    private Object lock = new Object();

    private Thread t1 = new Thread(()->{
        try {
            Thread.sleep(1000);
            System.out.println(System.currentTimeMillis()+":t1:going to wait.");
            synchronized (lock) {
                lock.wait();
                System.out.println(System.currentTimeMillis()+":t1:限制区的一些操作...");
                Thread.sleep(2000);
                System.out.println(System.currentTimeMillis()+":t1:限制区的一些操作...");
            }
            System.out.println(System.currentTimeMillis()+":t1:wait end.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    });

    private Thread t2 = new Thread(()->{
        try {
            Thread.sleep(2000);
            System.out.println(System.currentTimeMillis()+":t2:to awake it.");
            synchronized (lock) {
                System.out.println(System.currentTimeMillis()+":t2 进入了 限制区。");
                lock.notify();
            }
            System.out.println(System.currentTimeMillis()+":t2:awake it?");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    });


    public static void main(String[] args) throws InterruptedException {
        WaitTest test = new WaitTest();
        test.t1.start();
        test.t2.start();
        test.t1.join();
        test.t2.join();
        System.out.println("end.");
    }

}
