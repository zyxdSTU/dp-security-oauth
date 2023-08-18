package org.zjvis.dp.security.oauth.service;

import static org.zjvis.dp.security.oauth.dto.Constant.CLIENT_SECRET;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.zjvis.dp.security.oauth.dto.ClientQuery;
import org.zjvis.dp.security.oauth.dto.OauthClientDetails;
import org.zjvis.dp.security.oauth.mapper.OauthClientDetailsMapper;

/**
 * @author zhouyu
 * @create 2023-07-31 11:12
 */
@Service
public class OauthClientDetailsService {

    @Resource
    private OauthClientDetailsMapper oauthClientDetailsMapper;

    public List<OauthClientDetails> selectByCreator(Integer creator) {
        return oauthClientDetailsMapper.selectByCondition(ClientQuery.builder()
                .creator(creator)
                .build());
    }

    public OauthClientDetails selectByClientId(String clientId) {
        List<OauthClientDetails> result = oauthClientDetailsMapper.selectByCondition(ClientQuery.builder()
                .clientId(clientId)
                .build());

        if(CollectionUtils.isEmpty(result) || result.size() != 1) {
            return null;
        }

        return result.get(0);
    }

    public void updateClientSecret(String clientId, String encodeSecret, String secretWithAsterisk) {
        OauthClientDetails oauthClientDetails = oauthClientDetailsMapper.selectByPrimaryKey(clientId);
        oauthClientDetails.setClientSecret(encodeSecret);
        String additionInformation = oauthClientDetails.getAdditionalInformation();
        JSONObject jsonObject = JSON.parseObject(additionInformation);
        if(Objects.isNull(jsonObject)) {
            jsonObject = new JSONObject();
        }
        jsonObject.put(CLIENT_SECRET, secretWithAsterisk);
        oauthClientDetails.setAdditionalInformation(jsonObject.toJSONString());
        oauthClientDetailsMapper.updateByPrimaryKeySelective(oauthClientDetails);
    }

    public void updateClient(OauthClientDetails oauthClientDetails) {
        oauthClientDetailsMapper.updateByPrimaryKeySelective(oauthClientDetails);
    }

    public void insertClient(OauthClientDetails oauthClientDetails) {
        oauthClientDetailsMapper.insertSelective(oauthClientDetails);
    }
}
