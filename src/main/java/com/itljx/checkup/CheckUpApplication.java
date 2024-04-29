package com.itljx.checkup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author jinxi
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableCaching //开启springCache缓存功能
public class CheckUpApplication {
    public static void main(String[] args) {
        SpringApplication.run(CheckUpApplication.class, args);
        log.info("项目启动成功...");
    }
}
