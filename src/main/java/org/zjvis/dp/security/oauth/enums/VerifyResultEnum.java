package org.zjvis.dp.security.oauth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhouyu
 * @create 2023-08-10 10:38
 */
@Getter
@AllArgsConstructor
public enum VerifyResultEnum {
    SUCCESS(1, "token正确"),

    FAILED(2, "token错误"),

    NO_PERMISSIONS(3, "权限不足"),

    EXPIRED(4, "token过期");

    private Integer value;

    private String description;

    public static String getDescription(Integer value) {
        for (VerifyResultEnum verifyResultEnum : VerifyResultEnum.values()) {
            if (verifyResultEnum.getValue().equals(value)) {
                return verifyResultEnum.getDescription();
            }
        }
        throw new IllegalArgumentException();
    }
}
