package com.ww.mylock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@SpringBootTest(classes = MyLockApplication.class)
public class LockTest {
    @Autowired
    LockService lockService;

    /**
     * 测试分布式锁(模拟秒杀)
     */
    @Test
    public void test1() throws Exception {

//        try {
//            lockService.seckill("20120508784");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        ExecutorService executorService = Executors.newFixedThreadPool(6);
        IntStream.range(0, 5).forEach(i -> executorService.submit(() -> {
            try {
                lockService.seckill("2022");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        executorService.awaitTermination(300, TimeUnit.SECONDS);
//        Thread.sleep(10000);
    }

    /**
     * 测试分布式锁(模拟秒杀)
     */
    @Test
    public void test2() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        IntStream.range(0, 30).forEach(i -> executorService.submit(() -> {
            try {
                lockService.seckill2("20120508784");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }
}
