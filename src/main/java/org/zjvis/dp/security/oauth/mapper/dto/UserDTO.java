package org.zjvis.dp.security.oauth.mapper.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @description 用户基础信息
 * @date 2021-12-24
 */

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private Integer status;

    private String password;

    private String phone;

    private Integer role;

    private String nickName;

    private String realName;

    private String gender;

    private String profession;

    private String workOrg;

    private String address;

    private String researchField;

    private byte[] picEncode;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.dateTimeFormat, timezone = "GMT+8")
    private Date dateBirth;


    private String remark;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.dateTimeFormat, timezone = "GMT+8")
    protected LocalDateTime lastLoginTime;
}
