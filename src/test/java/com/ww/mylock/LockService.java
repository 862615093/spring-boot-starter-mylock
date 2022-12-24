package com.ww.mylock;

import com.ww.mylock.annotation.JLock;
import com.ww.mylock.client.RedissonLockClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LockService {

    @Resource
    private RedissonLockClient redissonLockClient;

    int n = 2;

    /**
     * 模拟秒杀(注解方式)
     */
    @JLock(lockKey = "#productId", expireSeconds = 5000)
    public void seckill(String productId) {
        if (n <= 0) {
            System.out.println(Thread.currentThread().getName() + ": 活动已结束,秒杀失败，请下次再来");
            return;
        }
        System.out.println(Thread.currentThread().getName() + ": 秒杀到了商品");
        System.out.println("商品剩余:" + (--n));
    }

    /**
     * 模拟秒杀(编程方式)
     */
    public void seckill2(String productId) {
        redissonLockClient.tryLock(productId, 5000);
        if (n <= 0) {
            System.out.println("活动已结束,请下次再来");
            return;
        }
        System.out.println(Thread.currentThread().getName() + ":秒杀到了商品");
        System.out.println(--n);
        redissonLockClient.unlock(productId);
    }
}
