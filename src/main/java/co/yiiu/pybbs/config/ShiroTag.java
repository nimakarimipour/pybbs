package co.yiiu.pybbs.config;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Component
public class ShiroTag {

    // aaaaaaaaaaaaaaa
    public boolean isAuthenticated() {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    // aaaaaaaaaa
    public String getPrincipal() {
        return (String) SecurityUtils.getSubject().getPrincipal();
    }

    // aaaaaaa xx aa
    public boolean hasRole(String name) {
        return SecurityUtils.getSubject().hasRole(name);
    }

    // aaaaaaa xx aa
    public boolean hasPermission(String name) {
        return !StringUtils.isEmpty(name) && SecurityUtils.getSubject().isPermitted(name);
    }

    // aaaaaaa xx aa
    public boolean hasPermissionOr(String... name) {
        boolean[] permitted = SecurityUtils.getSubject().isPermitted(name);
        for (boolean b : permitted) {
            // aaaaaaa，aaa
            if (b) {
                return true;
            }
        }
        return false;
    }

    // aaaaaaa xx aa
    public boolean hasPermissionAnd(String... name) {
        boolean[] permitted = SecurityUtils.getSubject().isPermitted(name);
        for (boolean b : permitted) {
            // aaaaaaaaa，aaa
            if (!b) {
                return false;
            }
        }
        return true;
    }

    // aaaaaaa xx aa
    public boolean hasAllPermission(String... name) {
        return SecurityUtils.getSubject().isPermittedAll(name);
    }
}
