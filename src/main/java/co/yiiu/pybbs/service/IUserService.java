package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.User;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface IUserService {
    // aaaaaaaaa，aaaaaaaaaaaaa
    User selectByUsername(String username);

    User addUser(String username, String password, String avatar, String email, String bio, String website,
                 boolean needActiveEmail);

    // aaaaaaa/aaaaaa
    User addUserWithMobile(String mobile);

    // aaaatokenaaaa
    User selectByToken(String token);

    // aaaamobileaaaa
    User selectByMobile(String mobile);

    // aaaaemailaaaa
    User selectByEmail(String email);

    User selectById(Integer id);

    User selectByIdWithoutCache(Integer id);

    // aaaaaaa
    List<User> selectTop(Integer limit);

    // aaaaaa
    void update(User user);

    IPage<User> selectAll(Integer pageNo, String username);

    User selectByIdNoCatch(Integer id);

    // aaaaaaaaaa
    int countToday();

    // aaaa
    void deleteUser(Integer id);

    // aaredisaa
    void delRedisUser(User user);
}
