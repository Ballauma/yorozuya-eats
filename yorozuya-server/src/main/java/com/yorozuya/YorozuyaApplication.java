package com.yorozuya;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Ballauma
 */
@SpringBootApplication
@EnableTransactionManagement // 开启注解方式的事务管理
@Slf4j
@EnableCaching
public class YorozuyaApplication {
    public static void main(String[] args) {
        SpringApplication.run(YorozuyaApplication.class, args);
        log.info("server started");
    }
}
