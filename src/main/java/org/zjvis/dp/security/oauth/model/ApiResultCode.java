package org.zjvis.dp.security.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhouyu
 * @create 2023-07-07 15:33
 */
@Getter
@AllArgsConstructor
public enum ApiResultCode {

    SUCCESS(200, "成功!"),

    SYS_ERROR(500, "服务端异常");

    private int code;

    private String message;
}
