package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.AdminUser;

import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface IAdminUserService {
    // aaaaaaaaa
    AdminUser selectByUsername(String username);

    // aaaaaaaaa
    List<Map<String, Object>> selectAll();

    void update(AdminUser adminUser);

    void insert(AdminUser adminUser);

    void delete(Integer id);

    AdminUser selectById(Integer id);

    // aaaaidaaaaaaaaa
    List<AdminUser> selectByRoleId(Integer roleId);
}
