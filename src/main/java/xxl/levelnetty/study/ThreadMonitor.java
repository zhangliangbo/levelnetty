package xxl.levelnetty.study;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhangliangbo
 * @since 2021/7/15
 **/


@Slf4j
public class ThreadMonitor {

    private static final ExecutorService SINGLE_THREAD_EXECUTOR = Executors.newSingleThreadExecutor();

    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

    public static void main(String[] args) {

        //基于println方法中的synchronize代码块测试运行或者监视线程
        Thread thread1 = new Thread(() -> {
            while (true) {
                System.out.println("运行或者监视线程1");
            }
        }, "运行或者监视线程1");
        thread1.start();

        //基于println方法中的synchronize代码块测试运行或者监视线程
        Thread thread2 = new Thread(() -> {
            while (true) {
                System.out.println("运行或者监视线程2");
            }
        }, "运行或者监视线程2");
        thread2.start();

        //monitor对象等待线程
        Object lock = new Object();
        Thread thread3 = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "等待线程synchronized");
        thread3.start();

        //reentrantLock中的条件对象调用await方法线程为驻留线程
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        Thread thread4 = new Thread(() -> {
            reentrantLock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }, "等待线程reentrantLock");
        thread4.start();

        //休眠线程
        Thread thread5 = new Thread(() -> {
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "休眠线程");
        thread5.start();

        Thread thread6 = new Thread(ThreadMonitor::lockMethod, "reentrantLock运行线程");
        thread6.start();

        //等待获取reentrantLock的线程为驻留线程
        Thread thread7 = new Thread(() -> {
            try {
                TimeUnit.MICROSECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lockMethod();
        }, "reentrantLock监视线程");
        thread7.start();

        //线程池中的空闲线程为驻留线程
        SINGLE_THREAD_EXECUTOR.execute(() -> {
            //线程池中的线程是懒加载，需要运行任务之后才会产生线程
            System.out.println("驻留线程运行");
        });
    }

    private static void lockMethod() {
        REENTRANT_LOCK.lock();
        try {
            while (true) {
            }
        } finally {
            REENTRANT_LOCK.unlock();
        }
    }

}
