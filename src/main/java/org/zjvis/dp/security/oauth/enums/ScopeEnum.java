package org.zjvis.dp.security.oauth.enums;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhouyu
 * @create 2023-08-02 10:25
 */
@Getter
@AllArgsConstructor
public enum ScopeEnum {
    USER_INFO("userInfo", "访问用户个人信息，包括用户id、邮箱和手机号"),

    PROJECT_INFO("projectInfo", "查看用户项目信息");

    private String name;

    private String description;

    public static String getDescription(String name) {
        if (Objects.isNull(name)) {
            return null;
        }
        for (ScopeEnum scopeEnum : ScopeEnum.values()) {
            if (scopeEnum.getName().equals(name)) {
                return scopeEnum.getDescription();
            }
        }
        throw new IllegalArgumentException();
    }
}
