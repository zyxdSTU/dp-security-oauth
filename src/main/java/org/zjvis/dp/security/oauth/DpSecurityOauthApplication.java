package org.zjvis.dp.security.oauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"org.zjvis.dp.security.oauth.mapper"})
public class DpSecurityOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(DpSecurityOauthApplication.class, args);
    }

}
