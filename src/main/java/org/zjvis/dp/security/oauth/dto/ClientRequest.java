package org.zjvis.dp.security.oauth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouyu
 * @create 2023-07-28 10:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "客户更新、注册参数")
public class ClientRequest {
    @ApiModelProperty(value = "clientId, 更新时需要传")
    private String clientId;

    @ApiModelProperty(value = "client名字")
    private String clientName;

    @ApiModelProperty(value = "client描述")
    private String clientDescription;

    @ApiModelProperty(value = "client主页")
    private String clientHomePage;

    @ApiModelProperty(value = "client redirectUrl")
    private String redirectUrl;

    @ApiModelProperty(value = "client请求权限")
    private List<String> scopeList;

    @ApiModelProperty(value = "创建人")
    private Integer creator;
}
