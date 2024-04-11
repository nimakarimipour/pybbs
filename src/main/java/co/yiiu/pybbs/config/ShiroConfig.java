package co.yiiu.pybbs.config;

import co.yiiu.pybbs.config.realm.MyCredentialsMatcher;
import co.yiiu.pybbs.config.realm.MyShiroRealm;
import co.yiiu.pybbs.service.ISystemConfigService;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Configuration
public class ShiroConfig {

    private final Logger log = LoggerFactory.getLogger(ShiroConfig.class);

    @Resource
    private MyShiroRealm myShiroRealm;
    @Resource
    private ISystemConfigService systemConfigService;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        log.info("aaaashiroFilter...");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //aaa.
        Map<String, String> map = new HashMap<>();

        // aaaaaaaaaa aaaa  aaaaaa
        map.put("/static/**", "anon");
        // aaaaaaaaaaaaaaaaaaa，aashiroaaaaaaaaa，aaaaaaaaaa
        map.put("/admin/login", "anon");
        map.put("/admin/logout", "anon");
        map.put("/admin/no_auth", "anon");

        //<!-- aaaaa，aaaaaaaa，aaa/**aaaaaa -->:aaaaaa，aaaaaaaaaaa;

        //<!-- authc:aaurlaaaaaaaaaaaa; user: aarememberMeaaaaaa anon:aaurlaaaaaaaa {@link org.apache.shiro.web.filter.mgt.DefaultFilter}-->
        map.put("/admin/permission/**", "perms");
        map.put("/admin/role/**", "perms");
        map.put("/admin/system/**", "perms");
        map.put("/admin/admin_user/**", "perms");

        map.put("/admin/**", "user");
//        map.put("/admin/comment/**", "user");
//        map.put("/admin/sensitive_word/**", "user");
//        map.put("/admin/tag/**", "user");
//        map.put("/admin/topic/**", "user");
//        map.put("/admin/user/**", "user");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        // aaaaaaaaaaaaWebaaaaaaa"/login.jsp"aa
        shiroFilterFactoryBean.setLoginUrl("/adminlogin");
        // aaaaaaaaaaa
        shiroFilterFactoryBean.setSuccessUrl("/admin/index");

        //aaaaa;
        shiroFilterFactoryBean.setUnauthorizedUrl("/admin/no_auth");

        return shiroFilterFactoryBean;
    }

    // aaaaaa
    // aaaaa，aaaaaaaa，，aaaaaaaa，aaaaaaaaaa
    @Bean
    public MyCredentialsMatcher myCredentialsMatcher() {
        return new MyCredentialsMatcher();
    }

    // aaaaaaa
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        myShiroRealm.setCredentialsMatcher(myCredentialsMatcher());
        securityManager.setRealm(myShiroRealm);
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    //aaaaaaa，aaaaaaaaaa
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager defaultWebSecurityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager);
        return authorizationAttributeSourceAdvisor;
    }

    // aaaaaaa
    @Bean
    @DependsOn("mybatisPlusConfig")
    public SimpleCookie rememberMeCookie() {
        //aaaaacookieaaa，aaaaacheckboxaname = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // aaacookieaaaa aaa
        int adminRememberMeMaxAge = Integer.parseInt(systemConfigService.selectAllConfig().get("admin_remember_me_max_age").toString());
        simpleCookie.setMaxAge(adminRememberMeMaxAge * 24 * 60 * 60);
        return simpleCookie;
    }

    @Bean
    @DependsOn("mybatisPlusConfig")
    public CookieRememberMeManager rememberMeManager() {
        //System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookieaaaaa aaaaaaaaaa aaAESaa aaaa(128 256 512 a)
        cookieRememberMeManager.setCipherKey(Base64.encode("pybbs is the best!".getBytes()));
        return cookieRememberMeManager;
    }

    @Bean
    public FormAuthenticationFilter formAuthenticationFilter() {
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        //aaaaacheckboxaname = rememberMe
        formAuthenticationFilter.setRememberMeParam("rememberMe");
        return formAuthenticationFilter;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

}
