package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.Permission;

import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface IPermissionService {

    // aaaaaaaaaaaaaa，aaaa，aaaaaaa
    void clearRolePermissionCache();

    // aaaaidaaaaaaa
    List<Permission> selectByRoleId(Integer roleId);

    // aaaaaaaaaa
    List<Permission> selectByPid(Integer pid);

    Map<String, List<Permission>> selectAll();

    Permission insert(Permission permission);

    Permission update(Permission permission);

    void delete(Integer id);
}
