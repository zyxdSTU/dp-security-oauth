package org.zjvis.dp.security.oauth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouyu
 * @create 2023-07-31 10:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "client列表响应")
public class ClientListResponse {
    @ApiModelProperty(value = "clientId", required = true)
    private String clientId;

    @ApiModelProperty(value = "client名称", required = true)
    private String clientName;
}
