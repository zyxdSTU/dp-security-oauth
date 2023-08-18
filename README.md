> 最近需要做一个统一登录平台，即需要实现我们平台的账号(第三方账号)，可以无缝 登录其他平台。然后前前后后调研Spring Security OAuth2.0两三周，有一些踩坑的 经验，记录分享一下。

## 什么是OAuth2.0协议

这篇文章讲得很细节，也很清楚，不了解OAuth2.0, 可以去研读一下

[Kane：白话让你理解什么是oAuth2协议](https://zhuanlan.zhihu.com/p/92051359)

## 怎么实现OAuth2.0协议

本身主要做java后端技术的，熟悉spring后端框架，所以主要调研了**Spring Security实现**

**OAuth2.0协议， 具体得实现了授权码模式**

## **talk is cheap, show your code**

### **前置步骤**

**application.yml配置好数据库**

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dp?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
```

在数据库中建好下列表

[Spring Security Oauth2 官方表结构解析，字段详解](https://blog.csdn.net/yangxiao_hui/article/details/109100140)

还有一张user表

```mysql
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(50) NOT NULL COMMENT '登录用户名',
  `email` varchar(100)  NOT NULL DEFAULT '' COMMENT '用户email',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '用户状态：1->正常 2->封禁中',
  `role` tinyint(4) NOT NULL DEFAULT 3 COMMENT '用户角色：1->超级管理员 2->系统管理员 3->普通用户',
  `password` varchar(255)  NOT NULL DEFAULT '',
  `phone` varchar(11) NOT NULL DEFAULT '' COMMENT '手机号',
  `remark` varchar(100)  NOT NULL DEFAULT '' COMMENT '备注',
  `nick_name` varchar(50) NULL DEFAULT '' COMMENT '昵称',
  `real_name` varchar(50) NULL DEFAULT '' COMMENT '真实姓名',
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4;
```

### 代码解析

**AuthorizationServerConfig:认证服务器配置类，很重要的一个类**

1、配置令牌端点的安全约束

2、配置客户端详情服务

3、配置令牌的访问端点以及令牌服务

注意``configure(AuthorizationServerEndpointsConfigurer endpoints)`` 方法里面可以对

认证的endpoint进行替换比如，讲confirm_access endpoint换成自己定制的

``endpoints.pathMapping(CONFIRM_ACCESS_URL, "/custom/confirm_access"); ``

**WebSecurityConfig:web安全配置类**

**1、配置相应的url绕过登录验证，包括静态资源、swagger资源，以及登录页面等**

```java
    protected static final String[] PERMIT_ALL_URL = {
            "/user/**",
            "/actuator/**",
            "/error",
            "/open/api",
            "/login*",
            "/css/**",
            "/**/*.svg",
            "/doc.html#/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/v2/**",
            "/swagger-resources/**",
            "/miserver/**",
            "/client/**",
            "/authorize/**"
    };   
 @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(PERMIT_ALL_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login");

    }
```

2、配置密码加密解密服务，`` passwordEncoder``

3、配置客户服务``oauthAccountUserDetailsService ``

### 引入jwt token配置

主要是在``TokenEnhancerChain``里面增加``jwtAccessTokenConverter``

```java
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
```

### 定制化授权页面

AuthorizationServerConfig里面替换了endpoint后，自己实现相应controller

```java
@SessionAttributes("authorizationRequest")
@Controller
public class GrantController {

    @RequestMapping("/custom/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.setViewName("grant");
        view.addObject("clientId", authorizationRequest.getClientId());
        Map<String, String> scopeMap = Maps.newHashMap();
        for (String scope : authorizationRequest.getScope()) {
            scopeMap.put(scope, ScopeEnum.getDescription(scope));
        }
        view.addObject("scopeMap", scopeMap);
        return view;
    }
}
```

## **接口文档**

### **内容交互图**

![img](https://picx.zhimg.com/80/v2-7d07e00dbb7f1831b152b0913efb5db0_1440w.png?source=d16d100b)

### **时序图**

![img](https://picx.zhimg.com/80/v2-cb5e0c4013b1a3c1bb9b11b148cde9c7_1440w.png?source=d16d100b)

### **1、获取授权码**

### **请求URL**

GET: /oauth/authorize

### **参数**

|               |      |        |                         |
| ------------- | ---- | ------ | ----------------------- |
| 参数名        | 必选 | 类型   | 说明                    |
| clientId      | 是   | String | client_id, 与数据库一致 |
| response_type | 是   | String | 固定传值code            |
| grant_type    | 是   | String | 固定authorization_code  |

### **示例**

数据库注册的client的数据

|                      |                                                              |                      |                                                              |                         |
| -------------------- | ------------------------------------------------------------ | -------------------- | ------------------------------------------------------------ | ----------------------- |
| client_id            | client_secret                                                | scope                | authorized_grant_types                                       | web_server_redirect_uri |
| 3QT4G1gwIeY5lGk7JvbL | $2a$10$Cu/.pI/U7nPI/hznw0Nhe.vhJY/pp1lYRtOeSBbXIgsWi5zxyTCLm | userInfo,projectInfo | client_credentials,authorization_code,password,refresh_token,implicit | https://www.baidu.com   |

**postman请求**

![img](https://picx.zhimg.com/80/v2-68f1132a90e548f4574b10747cf4485b_1440w.png?source=d16d100b)



**跳转获取code**

![img](https://picx.zhimg.com/80/v2-70f4348bfdc94f169e668676df20979c_1440w.png?source=d16d100b)

### **2、基于授权码获取access_token**

### **请求URL**

POST: /oauth/token

### **参数**

|               |      |        |                                                          |
| ------------- | ---- | ------ | -------------------------------------------------------- |
| 参数名        | 必选 | 类型   | 说明                                                     |
| code          | 是   | String | 上一步获取的授权码，例Gv8ez6                             |
| grant_type    | 是   | String | 固定authorization_code                                   |
| redirect_uri  | 是   | String | 重定向地址，与数据库一致https://www.baidu.com            |
| client_id     | 是   | String | client注册时，系统生成的client_id                        |
| client_secret | 是   | String | client注册时，系统生成的client_secret,需要client自己保存 |

注意：填client_id、client_secret

### **示例**

**post请求**

![img](https://pic1.zhimg.com/80/v2-68a063e346b10c9b118c53f38f5ca113_1440w.png?source=d16d100b)

**响应**

|               |        |                                                              |
| ------------- | ------ | ------------------------------------------------------------ |
| 响应名        | 类型   | 说明                                                         |
| access_token  | String | access_token，后续可以基于此，访问系统资源                   |
| token_type    | String | 固定bearer                                                   |
| refresh_token | String | access_token是会过期的，后续可以通过refresh_token获取新的access_token |
| expires_in    | String | access_token过期时间，例子是2小时后                          |
| scope         | String | access_token的权限范围                                       |
| user_info     | String | 可以定制的额外信息                                           |

```
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTA4Nzg4OTYsInVzZXJfbmFtZSI6InJvb3QxIiwianRpIjoiZTAwYWY4ZWMtOTYwMC00OGE5LTg3MDEtYTNlZDIxMDNkOWY1IiwiY2xpZW50X2lkIjoiM1FUNEcxZ3dJZVk1bEdrN0p2YkwiLCJzY29wZSI6WyJ1c2VySW5mbyIsInByb2plY3RJbmZvIl19.xmfljQs0aKqrZit75MamM-QooN4LT0noLYoCOD7r900",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTExMzA4OTYsInVzZXJfbmFtZSI6InJvb3QxIiwianRpIjoiZDRlZmNlMTAtYTM4Yi00ZTBiLTg2OGMtMjc2MDYyMDMyMWZhIiwiY2xpZW50X2lkIjoiM1FUNEcxZ3dJZVk1bEdrN0p2YkwiLCJzY29wZSI6WyJ1c2VySW5mbyIsInByb2plY3RJbmZvIl0sImF0aSI6ImUwMGFmOGVjLTk2MDAtNDhhOS04NzAxLWEzZWQyMTAzZDlmNSJ9.rYkIkv_hvUSe-rhas9mFyK0x7-Meqm3mbpYjKWLJKpA",
    "expires_in": 7199,
    "scope": "userInfo projectInfo",
    "user_info": {
        "username": "root1",
        "userId": 19,
        "email": "",
        "phone": "15869010599"
    }
}
```

### **3、刷新access_token**

### **请求URL**

POST: /oauth/token

### **参数**

|               |      |        |                                                          |
| ------------- | ---- | ------ | -------------------------------------------------------- |
| 参数名        | 必选 | 类型   | 说明                                                     |
| refresh_token | 是   | String | 上一步获取的refresh_token                                |
| grant_type    | 是   | String | 固定值refresh_token                                      |
| client_id     | 是   | String | client注册时，系统生成的client_id                        |
| client_secret | 是   | String | client注册时，系统生成的client_secret,需要client自己保存 |

### **示例**

postman请求

![img](https://picx.zhimg.com/80/v2-0abec23a19521e471991a4d9ca6ce875_1440w.png?source=d16d100b)



响应和获取access_token一样，就不缀叙了