package org.zjvis.dp.security.oauth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouyu
 * @create 2023-07-31 10:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "clientResponse")
public class ClientResponse {
    @ApiModelProperty(value = "client名字", required = true)
    private String clientName;

    @ApiModelProperty(value = "client描述")
    private String clientDescription;

    @ApiModelProperty(value = "client主页", required = true)
    private String clientHomePage;

    @ApiModelProperty(value = "client redirectUrl", required = true)
    private String redirectUrl;

    @ApiModelProperty(value = "client请求权限", required = true)
    private List<String> scopeList;

    @ApiModelProperty(value = "clientId", required = true)
    private String clientId;

    @ApiModelProperty(value = "clientSecret", required = true)
    private String clientSecret;
}
