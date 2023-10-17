package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.ucr.cs.riple.taint.ucrtainting.qual.RUntainted;

import java.util.List;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface IUserService {
    // 根据用户名查询用户，用于获取用户的信息比对密码
    @RUntainted User selectByUsername(String username);

    @RUntainted User addUser(String username, String password, String avatar, String email, String bio, String website,
                 boolean needActiveEmail);

    // 通过手机号登录/注册创建用户
    @RUntainted User addUserWithMobile(String mobile);

    // 根据用户token查询用户
    @RUntainted User selectByToken(String token);

    // 根据用户mobile查询用户
    @RUntainted User selectByMobile(String mobile);

    // 根据用户email查询用户
    @RUntainted User selectByEmail(String email);

    @RUntainted User selectById(@RUntainted Integer id);

    @RUntainted User selectByIdWithoutCache(@RUntainted Integer id);

    // 查询用户积分榜
    List<User> selectTop(Integer limit);

    // 更新用户信息
    void update(@RUntainted User user);

    IPage<User> selectAll(Integer pageNo, String username);

    @RUntainted User selectByIdNoCatch(@RUntainted Integer id);

    // 查询今天新增的话题数
    int countToday();

    // 删除用户
    void deleteUser(@RUntainted Integer id);

    // 删除redis缓存
    void delRedisUser(User user);
}
