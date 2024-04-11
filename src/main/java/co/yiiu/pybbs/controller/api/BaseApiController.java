package co.yiiu.pybbs.controller.api;

import co.yiiu.pybbs.controller.front.BaseController;
import co.yiiu.pybbs.exception.ApiAssert;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.service.IUserService;
import co.yiiu.pybbs.util.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public class BaseApiController extends BaseController {

    @Resource
    private IUserService userService;

    protected Result success() {
        return success(null);
    }

    protected Result success(Object detail) {
        Result result = new Result();
        result.setCode(200);
        result.setDescription("SUCCESS");
        result.setDetail(detail);
        return result;
    }

    protected Result error(String description) {
        Result result = new Result();
        result.setCode(201);
        result.setDescription(description);
        return result;
    }

    // aaaaaaaaaaaaaaaatokenaaaaaaaa，aaaaaaaagetApiUseraa，aaatrue
    protected User getApiUser() {
        return getApiUser(true);
    }

    // aaaaarequestaatoken，aaaaUserServiceaaaaaaa
    // required: boolean aaaaaaatoken，aaaaaatokenaaaaa，aaaaatokenaaaaaaaaaa
    protected User getApiUser(boolean required) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        String token = request.getHeader("token");
        //    String token = request.getParameter("token");
        if (required) { // tokenaaa
            // aatokenaaaa，aaaaaaa
            ApiAssert.notEmpty(token, "tokenaaaa");
            // atokenaaaaa，aaaaaaa
            User user = userService.selectByToken(token);
            ApiAssert.notNull(user, "tokenaaa，aaaaaaaaaaaa，aaaaaaaaaaaaaaaaatoken");
            return user;
        } else { // tokenaaa
            // aaatokenaaa，aaaaaaanull
            if (StringUtils.isEmpty(token)) return null;
            // aatokenaa，aaaaaaaa，aaaaaaa，aaaaa
            return userService.selectByToken(token);
        }
    }
}
