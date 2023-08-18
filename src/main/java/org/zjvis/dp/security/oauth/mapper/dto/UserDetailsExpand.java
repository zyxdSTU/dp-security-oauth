package org.zjvis.dp.security.oauth.mapper.dto;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author zhouyu
 * @create 2023-07-24 14:59
 */

public class UserDetailsExpand extends User {

    private String email;
    private String phone;

    public UserDetailsExpand(
            UserDTO userDTO,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(userDTO.getUsername(), userDTO.getPassword(), authorities);
        this.email = userDTO.getEmail();
        this.phone = userDTO.getPhone();
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
