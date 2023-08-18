package org.zjvis.dp.security.oauth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouyu
 * @create 2023-07-31 14:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "RegenerateClientSecret")
public class ClientIdRequest {
    @ApiModelProperty(value = "clientId", required = true)
    private String clientId;

}
