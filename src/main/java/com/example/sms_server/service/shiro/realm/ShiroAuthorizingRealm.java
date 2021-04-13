package com.example.sms_server.service.shiro.realm;


import com.example.sms_server.entity.BasUser;
import com.example.sms_server.service.BasUserService;
import com.example.sms_server.service.shiro.ShiroUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("shiroRealm")
public class ShiroAuthorizingRealm extends AuthorizingRealm {
    @Autowired
    private BasUserService basUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;

    }

    /**
     * 身份认证信息
     * 重写该方法，从数据库中获取注册用户和对应的密码，封装成AuthenticationInfo对象返回
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // authenticationToken 当前登录用户的身份标识
        //已知用户id
        String userId = authenticationToken.getPrincipal().toString();
        // 再根据用户id从数据库中找密码
        BasUser basUser = this.basUserService.getById(userId);
        if (basUser == null) {
            throw new AccountException("account not exist");
        }
        String password = basUser.getUserPassword();
        // 用AuthenticationInfo对象，封装username和password
        ShiroUser shiroUser = new ShiroUser();
        shiroUser.setId(basUser.getUserId());
        shiroUser.setUserName(basUser.getUserName());
        shiroUser.setPassword(password);
        //设置缓存关键字name+id
        shiroUser.setAuthCacheKey(basUser.getUserName() + basUser.getUserId());
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(shiroUser, password, this.getName());
        return info;
    }
}
