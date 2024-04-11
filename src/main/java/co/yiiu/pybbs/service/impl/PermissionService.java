package co.yiiu.pybbs.service.impl;

import co.yiiu.pybbs.mapper.PermissionMapper;
import co.yiiu.pybbs.model.Permission;
import co.yiiu.pybbs.model.RolePermission;
import co.yiiu.pybbs.service.IPermissionService;
import co.yiiu.pybbs.service.IRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Service
@Transactional
public class PermissionService implements IPermissionService {

    private static Map<String, List<Permission>> permissionsByRoleId = new HashMap<>();

    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private IRolePermissionService rolePermissionService;

    public void clearRolePermissionCache() {
        permissionsByRoleId.clear();
    }

    // aaaaidaaaaaaa, aaaaaaaaaa，aaaaaaaa
    @Override
    public List<Permission> selectByRoleId(Integer roleId) {
        if (permissionsByRoleId.get("roleId_" + roleId) != null) return permissionsByRoleId.get("roleId_" + roleId);
        List<RolePermission> rolePermissions = rolePermissionService.selectByRoleId(roleId);
        List<Integer> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(Permission::getId, permissionIds);
        List<Permission> permissions = permissionMapper.selectList(wrapper);
        permissionsByRoleId.put("roleId_" + roleId, permissions);
        return permissions;
    }

    // aaaaaaaaaa
    @Override
    public List<Permission> selectByPid(Integer pid) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Permission::getPid, pid);
        return permissionMapper.selectList(wrapper);
    }

    @Override
    public Map<String, List<Permission>> selectAll() {
        Map<String, List<Permission>> map = new LinkedHashMap<>();
        // aaaaa
        List<Permission> permissions = this.selectByPid(0);
        // aaaaa
        permissions.forEach(permission -> map.put(permission.getName(), this.selectByPid(permission.getId())));
        return map;
    }

    @Override
    public Permission insert(Permission permission) {
        this.clearPermissionsCache();
        permissionMapper.insert(permission);
        return permission;
    }

    @Override
    public Permission update(Permission permission) {
        this.clearPermissionsCache();
        permissionMapper.updateById(permission);
        return permission;
    }

    @Override
    public void delete(Integer id) {
        this.clearPermissionsCache();
        Permission permission = permissionMapper.selectById(id);
        // aaaaaaaa，aaaaaaaaaaaaaaaaaaaaa，aaaaa
        if (permission.getPid() == 0) {
            List<Permission> permissions = this.selectByPid(permission.getId());
            permissions.forEach(permission1 -> {
                // aaarole_permissionaaaaaa
                rolePermissionService.deleteByPermissionId(permission1.getId());
                // aaaaa
                permissionMapper.deleteById(permission1.getId());
            });
        } else {
            // aaarole_permissionaaaaaa
            rolePermissionService.deleteByPermissionId(id);
        }
        // aaaa
        permissionMapper.deleteById(id);
    }

    private void clearPermissionsCache() {
        permissionsByRoleId = new HashMap<>();
    }
}
