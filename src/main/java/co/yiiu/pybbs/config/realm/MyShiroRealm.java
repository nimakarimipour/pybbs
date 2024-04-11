package co.yiiu.pybbs.config.realm;

import co.yiiu.pybbs.model.AdminUser;
import co.yiiu.pybbs.model.Permission;
import co.yiiu.pybbs.model.Role;
import co.yiiu.pybbs.service.IAdminUserService;
import co.yiiu.pybbs.service.IPermissionService;
import co.yiiu.pybbs.service.IRoleService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Component
public class MyShiroRealm extends AuthorizingRealm {

    private final Logger log = LoggerFactory.getLogger(MyShiroRealm.class);

    @Resource
    private IAdminUserService adminUserService;
    @Resource
    private IRoleService roleService;
    @Resource
    private IPermissionService permissionService;

    // aaaaaa
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //aa@RequirePermissionaaaurlaaa
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        AdminUser adminUser = adminUserService.selectByUsername(principals.toString());
        //aaaaaaa，aaaaaaa
        Role role = roleService.selectById(adminUser.getRoleId());
        // aaaaaaaaaaaaaaa，aaaaaaaaaaaa，
        // aaaaaaaaaaa，aaaaaaaaaa，aaaaaaaaaaaaaaAuthorizationInfoa
        simpleAuthorizationInfo.addRole(role.getName());
        // aaaa
        List<Permission> permissions = permissionService.selectByRoleId(adminUser.getRoleId());
        // aaaaaaaaaaaaaaaaStringaaa
        List<String> permissionValues = permissions.stream().map(Permission::getValue).collect(Collectors.toList());
        // aaaaStringaaaaaAuthorizationInfoa，aaaaaaaa
        simpleAuthorizationInfo.addStringPermissions(permissionValues);
        return simpleAuthorizationInfo;
    }

    // aaaaaa
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        log.info("aa：{} aaaa...", username);
        AdminUser adminUser = adminUserService.selectByUsername(username);
        // aaaaaaa，aaaaaaaaaa
        if (adminUser == null) throw new UnknownAccountException();
        return new SimpleAuthenticationInfo(username, adminUser.getPassword(), getName());
    }

}
