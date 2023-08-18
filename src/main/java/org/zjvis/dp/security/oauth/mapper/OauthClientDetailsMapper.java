package org.zjvis.dp.security.oauth.mapper;

import java.util.List;
import org.zjvis.dp.security.oauth.dto.ClientQuery;
import org.zjvis.dp.security.oauth.dto.OauthClientDetails;

public interface OauthClientDetailsMapper {
    int deleteByPrimaryKey(String clientId);

    int insert(OauthClientDetails record);

    int insertSelective(OauthClientDetails record);

    OauthClientDetails selectByPrimaryKey(String clientId);

    int updateByPrimaryKeySelective(OauthClientDetails record);

    int updateByPrimaryKey(OauthClientDetails record);

    List<OauthClientDetails> selectByCondition(ClientQuery clientQuery);
}