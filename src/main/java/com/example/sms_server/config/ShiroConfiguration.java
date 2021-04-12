package cn.com.authority.config;

import cn.com.authority.entity.AuthSysPermission;
import cn.com.authority.service.IPermissionService;
import cn.com.authority.service.shiro.filter.PermissionAuthFilter;
import cn.com.authority.service.shiro.filter.TokenAuthFilter;
import cn.com.authority.service.shiro.realm.ShiroAuthorizingRealm;
import cn.com.authority.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class ShiroConfiguration {
    @Autowired
    private RedisUtil redisUtil;

    private TokenAuthFilter tokenAuthFilter;

    private PermissionAuthFilter permissionAuthFilter;
    @Autowired
    private IPermissionService permissionService;

    private static final String CACHE_KEY = "shiro:cache:";
    private static final String SESSION_KEY = "shiro:session:";
    private static final String NAME = "custom.name";
    private static final String VALUE = "/";

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 创建一个Map对象包含所有要集成到Shiro框架中去的filter对象
        // 注册哪些过滤器
        Map<String, Filter> filters = new HashMap<String, Filter>();
        // <filter>
        // <filter-mapping
        this.tokenAuthFilter = new TokenAuthFilter();
        this.tokenAuthFilter.setRedisUtil(this.redisUtil);
        this.permissionAuthFilter = new PermissionAuthFilter();

        filters.put("authcToken", this.tokenAuthFilter);
        filters.put("permAuthc", this.permissionAuthFilter);

        // 在shiro中注册过滤器
        shiroFilterFactoryBean.setFilters(filters);

        // 设置哪些过滤器拦截哪些请求
        Map<String,String> permChainMap = new HashMap<>();
        //permChainMap.put("/user/**", "tokenAuthc");
        List<AuthSysPermission> permissionList = this.permissionService.searchPermissions();
        if(permissionList != null){
            for(AuthSysPermission permission:permissionList){// /user/**
                permChainMap.put("/"+permission.getResources().getRscUrl()+"/**", "authcToken, permAuthc");
            }
        }

        shiroFilterFactoryBean.setFilterChainDefinitionMap(permChainMap);

        // 其它的设置
        shiroFilterFactoryBean.setLoginUrl("/auth/login");
        //shiroFilterFactoryBean.setSuccessUrl();
        shiroFilterFactoryBean.setUnauthorizedUrl("/auth/noAuth");

        shiroFilterFactoryBean.setSecurityManager(securityManager);
        return shiroFilterFactoryBean;
    }

    @Bean("securityManager") // 固定！
    public DefaultWebSecurityManager createSecurityManager(@Qualifier("shiroRealm") ShiroAuthorizingRealm shiroRealm){
        // 创建SecurityMananger对象，并且装配Realm对象
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager(shiroRealm);
        // 自定义缓存实现 使用redis
        manager.setCacheManager(cacheManager());
        // 自定义session管理 使用redis
        manager.setSessionManager(sessionManager());

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);

        return manager;
    }

    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        // 允许支持基于Cookie传递的会话id
        sessionManager.setSessionIdCookieEnabled(true);
        // 支持请求的url重新（通过url传递会话id）
        sessionManager.setSessionIdUrlRewritingEnabled(true);

        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName(NAME);
        simpleCookie.setValue(VALUE);
        // 设置传递会话id的cookie的属性
        sessionManager.setSessionIdCookie(simpleCookie);

        return sessionManager;
    }

    // 准备RedisSessionDAO
    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setExpire(86400);
        redisSessionDAO.setKeyPrefix(SESSION_KEY);
        return redisSessionDAO;
    }
    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        // 装配redisManager
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setExpire(86400);
        redisCacheManager.setKeyPrefix(CACHE_KEY);
        return redisCacheManager;
    }
    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("127.0.0.1");
        redisManager.setPort(6379);
        redisManager.setTimeout(0);
        // redisManager.setPassword(password);
        return redisManager;
    }
}
