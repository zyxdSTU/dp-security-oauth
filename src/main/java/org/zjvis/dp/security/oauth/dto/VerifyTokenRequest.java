package org.zjvis.dp.security.oauth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouyu
 * @create 2023-08-02 10:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "校验token参数")
public class VerifyTokenRequest {
    @ApiModelProperty(value = "accessToken", required = true)
    private String accessToken;
}
