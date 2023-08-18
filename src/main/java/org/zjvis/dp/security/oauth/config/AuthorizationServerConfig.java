package org.zjvis.dp.security.oauth.config;

import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

/**
 * @author zhouyu
 * @create 2023-07-21 15:14
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Resource
    private JdbcClientDetailsService jdbcClientDetailsService;

    @Resource
    private DefaultTokenServices defaultTokenServices;

    @Resource
    private AuthenticationManager authenticationManagerBean;

    public static final String CONFIRM_ACCESS_URL = "/oauth/confirm_access";


    /**
     * 用来配置令牌端点的安全约束
     * @param security a fluent configurer for security features
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 用来配置客户端详情服务
     * @param clients the client details configurer
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService);
    }


    /**
     * 用来配置令牌的访问端点和令牌服务
     * @param endpoints the endpoints configurer
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenServices(defaultTokenServices);
        //密码校验
        endpoints.authenticationManager(authenticationManagerBean);

        endpoints.pathMapping(CONFIRM_ACCESS_URL, "/custom/confirm_access");
    }
}
