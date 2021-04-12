package cn.com.authority.service.shiro.realm;

import cn.com.authority.entity.AuthSysUser;
import cn.com.authority.service.IUserService;
import cn.com.authority.service.shiro.ShiroUser;
import cn.com.authority.utils.EncryptUtil;
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
    private IUserService userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 已知用户名
//        String username = principalCollection.getPrimaryPrincipal().toString();
        ShiroUser shiroUser = (ShiroUser) principalCollection.getPrimaryPrincipal();
        String username = shiroUser.getUserName();
        //  资源：操作：id     user:create
        List<String> permissionList = this.userService.getUserPermssions(username);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissionList);

        return simpleAuthorizationInfo;
    }

    /**
     * 身份认证信息
     * 重写该方法，从数据库中获取注册用户和对应的密码，封装成AuthenticationInfo对象返回
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // authenticationToken 当前登录用户的身份标识
        // 已知用户名
        String username = authenticationToken.getPrincipal().toString();
        // 再根据用户名从数据库中找密码
        AuthSysUser user = this.userService.findByUserName(username);
        if(user == null){
            throw new AccountException("account not exist");
        }
        String password = user.getUrPassword();
        // 用AuthenticationInfo对象，封装username和password
        ShiroUser shiroUser = new ShiroUser();
        shiroUser.setId(user.getUrId());
        shiroUser.setUserName(user.getUrUserName());
        shiroUser.setPassword(password);
        shiroUser.setAuthCacheKey(user.getUrUserName()+user.getUrId());
        SimpleAuthenticationInfo info= new SimpleAuthenticationInfo(shiroUser, password, this.getName());
        return info;
    }
}
