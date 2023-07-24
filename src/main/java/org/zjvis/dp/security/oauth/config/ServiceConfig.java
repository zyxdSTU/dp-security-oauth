package org.zjvis.dp.security.oauth.config;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

/**
 * @author zhouyu
 * @create 2023-07-21 15:20
 */
@Configuration
public class ServiceConfig {
    @Resource
    private DataSource dataSource;

    /**
     * 配置client详情服务
     * @return
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }


    /**
     * 令牌存储方式
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }


    /**
     * 令牌服务
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(jdbcClientDetailsService());
        // 允许支持refreshToken
        tokenServices.setSupportRefreshToken(true);
        // refreshToken 不重用策略
        tokenServices.setReuseRefreshToken(false);
        //设置Token存储方式
        tokenServices.setTokenStore(tokenStore());
        return tokenServices;
    }

}
