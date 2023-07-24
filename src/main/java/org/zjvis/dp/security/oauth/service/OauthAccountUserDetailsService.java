package org.zjvis.dp.security.oauth.service;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zjvis.dp.security.oauth.dto.UserDTO;
import org.zjvis.dp.security.oauth.dto.UserDetailsExpand;
import org.zjvis.dp.security.oauth.mapper.UserMapper;

/**
 * @author zhouyu
 * @create 2023-07-21 15:52
 */
@Service
public class OauthAccountUserDetailsService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    /**
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userMapper.selectByUserName(username);
        if (Objects.isNull(userDTO)) {
            throw new UsernameNotFoundException(String.format("%s don't exists", username));
        }
        List<SimpleGrantedAuthority> authorities = Lists.newArrayList();
        return new UserDetailsExpand(userDTO, authorities);
    }
}
