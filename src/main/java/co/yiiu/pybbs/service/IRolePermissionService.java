package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.RolePermission;

import java.util.List;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface IRolePermissionService {
    // aaaaidaaaaaaaaaaaaa
    List<RolePermission> selectByRoleId(Integer roleId);

    // aaaaidaaaaaa
    void deleteByRoleId(Integer roleId);

    // aaaaidaaaaaa
    void deleteByPermissionId(Integer permissionId);

    void insert(RolePermission rolePermission);
}
