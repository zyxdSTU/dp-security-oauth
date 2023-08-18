package org.zjvis.dp.security.oauth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouyu
 * @create 2023-08-10 10:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "校验token结果")
public class VerifyTokenResponse {
    @ApiModelProperty(value = "值")
    private Integer value;

    @ApiModelProperty(value = "描述")
    private String description;
}
