package co.yiiu.pybbs.config.realm;

import co.yiiu.pybbs.util.bcrypt.BCryptPasswordEncoder;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public class MyCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // aa！！！！！！！！！！！！！！！！！！！
        // aatokenainfoaaaaaaaCredentialsaaaaObject，aaaaaaaaa char[]
        // aa！！！！！ tokenaaaStringaaaaa char[]
        // ainfoaaCredentialsaaaaaaa String.valueOf() aaString
        // aa。。
        String rawPassword = String.valueOf((char[]) token.getCredentials());
        String encodedPassword = String.valueOf(info.getCredentials());
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
