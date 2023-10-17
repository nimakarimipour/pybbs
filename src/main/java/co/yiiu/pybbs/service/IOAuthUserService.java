package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.OAuthUser;
import edu.ucr.cs.riple.taint.ucrtainting.qual.RUntainted;

import java.util.List;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface IOAuthUserService {
    OAuthUser selectByTypeAndLogin(String type, String login);

    List<OAuthUser> selectByUserId(Integer userId);

    void addOAuthUser(@RUntainted Integer oauthId, String type, String login, String accessToken, String bio, String email, @RUntainted Integer
            userId, String refreshToken, String unionId, String openId);

    void update(OAuthUser oAuthUser);
}
