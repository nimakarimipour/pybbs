package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.Code;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface ICodeService {
    Code selectByCode(String _code);

    // aaaaaaacode
    Code selectNotUsedCode(Integer userId, String email, String mobile);

    // aaaaaaaaa
    Code createCode(Integer userId, String email, String mobile);

    // aaaaaaa
    Code validateCode(Integer userId, String email, String mobile, String _code);

    // aaaa
    boolean sendEmail(Integer userId, String email, String title, String content);

    // aaaa
    boolean sendSms(String mobile);

    void update(Code code);

    // aaaaidaaaaaa
    void deleteByUserId(Integer userId);
}
