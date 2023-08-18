package org.zjvis.dp.security.oauth.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouyu
 * @create 2023-07-07 15:19
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 4407476938805266789L;

    private int code;

    private String message;

    private T data;

    public static <T> ApiResult<T> valueOf(T result) {
        return ApiResult.valueOf(ApiResultCode.SUCCESS, result);
    }

    public static <T> ApiResult<T> valueOf(ApiResultCode apiResultCode) {
        return ApiResult.valueOf(apiResultCode, null);
    }

    public static <T> ApiResult<T> error(ApiResultCode apiResultCode, String message) {
        return new ApiResult<T>(apiResultCode.getCode(), message, null);
    }

    public static <T> ApiResult<T> valueOf(ApiResultCode apiResultCode, T result) {
        return ApiResult.<T>builder()
                .code(apiResultCode.getCode())
                .message(apiResultCode.getMessage())
                .data(result)
                .build();

    }
}
