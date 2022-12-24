package com.ww.mylock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "com.ww.mylock")
@EnableAspectJAutoProxy
public class MyLockApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyLockApplication.class, args);
    }
}