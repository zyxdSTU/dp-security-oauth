package org.zjvis.dp.security.oauth.controller;

import static org.zjvis.dp.security.oauth.dto.Constant.SCOPE_KEY;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zjvis.dp.security.oauth.dto.VerifyTokenRequest;
import org.zjvis.dp.security.oauth.dto.VerifyTokenResponse;
import org.zjvis.dp.security.oauth.enums.ScopeEnum;
import org.zjvis.dp.security.oauth.enums.VerifyResultEnum;
import org.zjvis.dp.security.oauth.model.ApiResult;

/**
 * @author zhouyu
 * @create 2023-08-01 16:38
 */
@RestController
@RequestMapping("/authorize")
@Api(tags = "认证接口")
public class AuthorizeController {

    @Resource
    private DefaultTokenServices defaultTokenServices;

    @Resource
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @PostMapping(value = "/verify_access_token")
    @ApiOperation(value = "验证access_token")
    public ApiResult<VerifyTokenResponse> verifyAccessToken(@RequestBody VerifyTokenRequest request) {

        OAuth2AccessToken token = defaultTokenServices.readAccessToken(request.getAccessToken());
        if (token == null) {
            return ApiResult.valueOf(VerifyTokenResponse.builder()
                    .value(VerifyResultEnum.FAILED.getValue())
                    .description(VerifyResultEnum.FAILED.getDescription())
                    .build());
        }

        if (token.isExpired()) {
            return ApiResult.valueOf(VerifyTokenResponse.builder()
                    .value(VerifyResultEnum.EXPIRED.getValue())
                    .description(VerifyResultEnum.EXPIRED.getDescription())
                    .build());
        }

        OAuth2Authentication authentication = defaultTokenServices.loadAuthentication(token.getValue());

        Map<String, ?> mapResult = jwtAccessTokenConverter.convertAccessToken(token, authentication);

        if (!mapResult.containsKey(SCOPE_KEY)) {
            return ApiResult.valueOf(VerifyTokenResponse.builder()
                    .value(VerifyResultEnum.NO_PERMISSIONS.getValue())
                    .description(VerifyResultEnum.NO_PERMISSIONS.getDescription())
                    .build());
        }

        Set scopeValueSet = (Set) mapResult.get(SCOPE_KEY);

        if (scopeValueSet.contains(ScopeEnum.PROJECT_INFO.getName())) {
            return ApiResult.valueOf(VerifyTokenResponse.builder()
                    .value(VerifyResultEnum.SUCCESS.getValue())
                    .description(VerifyResultEnum.SUCCESS.getDescription())
                    .build());
        } else {
            return ApiResult.valueOf(VerifyTokenResponse.builder()
                    .value(VerifyResultEnum.NO_PERMISSIONS.getValue())
                    .description(VerifyResultEnum.NO_PERMISSIONS.getDescription())
                    .build());
        }
    }

}
