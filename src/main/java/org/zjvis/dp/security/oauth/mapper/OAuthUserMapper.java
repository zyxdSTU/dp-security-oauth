package org.zjvis.dp.security.oauth.mapper;

import org.zjvis.dp.security.oauth.dto.UserDTO;

public interface OAuthUserMapper {

    UserDTO selectByUserName(String userName);


}
