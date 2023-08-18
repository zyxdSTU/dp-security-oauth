package org.zjvis.dp.security.oauth.controller;

import static org.zjvis.dp.security.oauth.dto.Constant.CLIENT_DESCRIPTION;
import static org.zjvis.dp.security.oauth.dto.Constant.CLIENT_HOME_PAGE;
import static org.zjvis.dp.security.oauth.dto.Constant.CLIENT_NAME;
import static org.zjvis.dp.security.oauth.dto.Constant.CLIENT_SECRET;
import static org.zjvis.dp.security.oauth.dto.Constant.CREATOR;
import static org.zjvis.dp.security.oauth.util.SecurityOauthUtils.getStringWithAsterisk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zjvis.dp.security.oauth.dto.ClientIdRequest;
import org.zjvis.dp.security.oauth.dto.ClientListRequest;
import org.zjvis.dp.security.oauth.dto.ClientListResponse;
import org.zjvis.dp.security.oauth.dto.ClientRequest;
import org.zjvis.dp.security.oauth.dto.ClientResponse;
import org.zjvis.dp.security.oauth.dto.OauthClientDetails;
import org.zjvis.dp.security.oauth.model.ApiResult;
import org.zjvis.dp.security.oauth.service.OauthClientDetailsService;
import org.zjvis.dp.security.oauth.util.SecurityOauthUtils;

/**
 * @author zhouyu
 * @create 2023-07-28 10:43
 */
@RestController
@RequestMapping("/client")
@Api(tags = "client接口")
public class ClientController {
    @Resource
    private OauthClientDetailsService oauthClientDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/registerClient")
    @ApiOperation(value = "client注册、更新接口")
    public ApiResult<ClientResponse> registerClient(@RequestBody ClientRequest clientRequest) {
        OauthClientDetails clientDetails = new OauthClientDetails();

        clientDetails.setScope(StringUtils.join(clientRequest.getScopeList(), ","));
        clientDetails.setWebServerRedirectUri(clientRequest.getRedirectUrl());
        Map<String, Object> additionalInformation = Maps.newHashMap();
        additionalInformation.put(CLIENT_HOME_PAGE, clientRequest.getClientHomePage());
        additionalInformation.put(CLIENT_DESCRIPTION, clientRequest.getClientDescription());
        additionalInformation.put(CLIENT_NAME, clientRequest.getClientName());
        additionalInformation.put(CREATOR, clientRequest.getCreator());

        //更新操作
        if(StringUtils.isNotBlank(clientRequest.getClientId())) {
            OauthClientDetails originDetail = oauthClientDetailsService.selectByClientId(clientRequest.getClientId());
            clientDetails.setClientId(clientRequest.getClientId());
            JSONObject jsonObject = JSONObject.parseObject(originDetail.getAdditionalInformation());
            if(Objects.nonNull(jsonObject) && jsonObject.containsKey(CLIENT_SECRET)) {
                additionalInformation.put(CLIENT_SECRET, jsonObject.getString(CLIENT_SECRET));
            }
            clientDetails.setAdditionalInformation(JSON.toJSONString(additionalInformation));
            oauthClientDetailsService.updateClient(clientDetails);
            //回显需要
            if(Objects.nonNull(jsonObject) && jsonObject.containsKey(CLIENT_SECRET)) {
                clientDetails.setClientSecret(jsonObject.getString(CLIENT_SECRET));
            }
        } else {
            //插入操作
            clientDetails.setClientId(SecurityOauthUtils.getStringRandom(20));
            String clientSecret = SecurityOauthUtils.getStringRandom(40);
            clientDetails.setClientSecret(passwordEncoder.encode(clientSecret));
            //默认支持四种授权模式
            clientDetails.setAuthorizedGrantTypes("client_credentials,authorization_code,password,refresh_token,implicit");
            //token有效时间，默认两小时
            clientDetails.setAccessTokenValidity(7200);
            clientDetails.setRefreshTokenValidity(259200);
            additionalInformation.put(CLIENT_SECRET, getStringWithAsterisk(clientSecret));
            clientDetails.setAdditionalInformation(JSON.toJSONString(additionalInformation));
            oauthClientDetailsService.insertClient(clientDetails);
            //第一次回显需要操作
            clientDetails.setClientSecret(clientSecret);
        }

        ClientResponse clientResponse = ClientResponse.builder()
                .clientName(clientRequest.getClientName())
                .clientDescription(clientRequest.getClientDescription())
                .clientHomePage(clientRequest.getClientHomePage())
                .redirectUrl(clientRequest.getRedirectUrl())
                .scopeList(clientRequest.getScopeList())
                .clientId(clientDetails.getClientId())
                .clientSecret(clientDetails.getClientSecret())
                .build();

        return ApiResult.valueOf(clientResponse);
    }

    @PostMapping(value = "/clientListQuery")
    @ApiOperation(value = "client列表查询")
    public ApiResult<List<ClientListResponse>> clientListQuery(@RequestBody ClientListRequest clientListRequest) {
        List<OauthClientDetails> oauthClientDetails = oauthClientDetailsService.selectByCreator(clientListRequest.getCreator());
        List<ClientListResponse> result = oauthClientDetails.stream()
                .map(element -> {
                    JSONObject jsonObject = JSONObject.parseObject(element.getAdditionalInformation());
                    String clientName = jsonObject.getString(CLIENT_NAME);
                    return ClientListResponse.builder()
                            .clientId(element.getClientId())
                            .clientName(clientName)
                            .build();
                }).collect(Collectors.toList());
        return ApiResult.valueOf(result);
    }

    @PostMapping(value = "/clientDetail")
    @ApiOperation(value = "client详情")
    public ApiResult<ClientResponse> clientDetail(@RequestBody ClientIdRequest clientIdRequest) {
        ClientResponse result = new ClientResponse();
        OauthClientDetails oauthClientDetails = oauthClientDetailsService.selectByClientId(clientIdRequest.getClientId());
        JSONObject jsonObject = JSONObject.parseObject(oauthClientDetails.getAdditionalInformation());
        List<String> scopeList = Lists.newArrayList();
        if(StringUtils.isNotBlank(oauthClientDetails.getScope())) {
            scopeList.addAll(Arrays.asList(oauthClientDetails.getScope().split(",")));
        }
        result.setScopeList(scopeList);
        if (Objects.nonNull(jsonObject)) {
            if (jsonObject.containsKey(CLIENT_SECRET)) {
                result.setClientSecret(jsonObject.getString(CLIENT_SECRET));
            }

            if (jsonObject.containsKey(CLIENT_HOME_PAGE)) {
                result.setClientHomePage(jsonObject.getString(CLIENT_HOME_PAGE));
            }

            if (jsonObject.containsKey(CLIENT_DESCRIPTION)) {
                result.setClientDescription(jsonObject.getString(CLIENT_DESCRIPTION));
            }

            if (jsonObject.containsKey(CLIENT_NAME)) {
                result.setClientName(jsonObject.getString(CLIENT_NAME));
            }
        }

        result.setRedirectUrl(oauthClientDetails.getWebServerRedirectUri());
        result.setClientId(oauthClientDetails.getClientId());

        return ApiResult.valueOf(result);
    }

    @PostMapping(value = "/regenerateClientSecret")
    @ApiOperation(value = "重新生成密钥")
    public ApiResult<String> regenerateClientSecret(@RequestBody ClientIdRequest clientIdRequest) {
        String clientSecret = SecurityOauthUtils.getStringRandom(40);
        String encodeSecret = passwordEncoder.encode(clientSecret);
        String secretWithAsterisk = SecurityOauthUtils.getStringWithAsterisk(clientSecret);
        oauthClientDetailsService.updateClientSecret(clientIdRequest.getClientId(), encodeSecret, secretWithAsterisk);
        return ApiResult.valueOf(clientSecret);
    }
}