package org.zjvis.dp.security.oauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"org.zjvis.dp.security.oauth.mapper"})
public class DpSecurityOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(DpSecurityOauthApplication.class, args);
    }

}
