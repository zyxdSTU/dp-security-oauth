package org.zjvis.dp.security.oauth.controller;

/**
 * @author zhouyu
 * @create 2023-07-25 15:19
 */

import com.google.common.collect.Maps;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.zjvis.dp.security.oauth.enums.ScopeEnum;

@SessionAttributes("authorizationRequest")
@Controller
public class GrantController {

    @RequestMapping("/custom/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.setViewName("grant");
        view.addObject("clientId", authorizationRequest.getClientId());
        Map<String, String> scopeMap = Maps.newHashMap();
        for (String scope : authorizationRequest.getScope()) {
            scopeMap.put(scope, ScopeEnum.getDescription(scope));
        }
        view.addObject("scopeMap", scopeMap);
        return view;
    }

}