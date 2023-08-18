package org.zjvis.dp.security.oauth.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.zjvis.dp.security.oauth.dto.UserDetailsExpand;

/**
 * @author zhouyu
 * @create 2023-07-21 15:20
 */
@Configuration
public class ServiceConfig {
    @Resource
    private DataSource dataSource;

    public static final String SIGNING_KEY = "dp-security-oauth";

    /**
     * 配置client详情服务
     * @return
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);
        return jwtAccessTokenConverter;
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
     * token 额外自定义信息 此例获取用户信息
     */
    @Bean
    public TokenEnhancer additionalInformationTokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> information = new HashMap<>(8);
            Authentication userAuthentication = authentication.getUserAuthentication();
            if (userAuthentication instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) userAuthentication;
                Object principal = token.getPrincipal();
                if (principal instanceof UserDetailsExpand) {
                    UserDetailsExpand userDetailsExpand = (UserDetailsExpand) token.getPrincipal();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> map = objectMapper.convertValue(userDetailsExpand, new TypeReference<Map<String, Object>>() {});
                    map.remove("password");
                    map.remove("authorities");
                    map.remove("accountNonExpired");
                    map.remove("accountNonLocked");
                    map.remove("credentialsNonExpired");
                    map.remove("enabled");
                    information.put("user_info", map);
                    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(information);
                }
            }
            return accessToken;
        };
    }


    /**
     * 令牌服务
     * @return
     */
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(jdbcClientDetailsService());
        // 允许支持refreshToken
        tokenServices.setSupportRefreshToken(true);
        // refreshToken 不重用策略
        tokenServices.setReuseRefreshToken(false);
        //设置Token存储方式
        tokenServices.setTokenStore(tokenStore());

        //设置成jwt token形式
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Lists.newArrayList(jwtAccessTokenConverter(), additionalInformationTokenEnhancer()));
        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        return tokenServices;
    }

}
