package org.zjvis.dp.security.oauth.dto;

import java.io.Serializable;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author zhouyu
 * @create 2023-07-24 14:59
 */

public class UserDetailsExpand extends User implements Serializable {

    private static final long serialVersionUID = -8749963173756275185L;
    private Long userId;
    private String email;
    private String phone;

    public UserDetailsExpand(
            UserDTO userDTO,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(userDTO.getUsername(), userDTO.getPassword(), authorities);
        this.email = userDTO.getEmail();
        this.phone = userDTO.getPhone();
        this.userId = userDTO.getId();
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }
}
