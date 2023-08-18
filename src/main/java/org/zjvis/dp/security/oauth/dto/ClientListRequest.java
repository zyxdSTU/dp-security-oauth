package org.zjvis.dp.security.oauth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouyu
 * @create 2023-07-31 10:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "client列表请求")
public class ClientListRequest {
    @ApiModelProperty(value = "创建人", required = true)
    private Integer creator;
}
