package org.zjvis.dp.security.oauth.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouyu
 * @create 2023-07-31 11:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "ClientQuery")
public class ClientQuery {
    private Integer creator;
    private String clientId;
}
