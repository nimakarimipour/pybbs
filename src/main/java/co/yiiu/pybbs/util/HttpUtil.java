package co.yiiu.pybbs.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public class HttpUtil {

    public static boolean isApiRequest(HttpServletRequest request) {
        return request.getHeader("Accept") == null || !request.getHeader("Accept").contains("text/html");
    }

    // aaaaaaaaaaaaaaaaaa
    // aaaaaarequestaaheaderaacceptaaaaaa
    // aaa text/html aaaaajs，aaaaresponseaaaaaaaaaaaaa text/javascript
    // aaa application/json aaaaajson，response aaaaaaaaaaaaa application/json
    // aaaaaaaaaaa，aaaaaa ;charset=utf-8 aaaaaa
    // aaaaaaa。。
    public static void responseWrite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!HttpUtil.isApiRequest(request)) {
            response.setContentType("text/html;charset=utf-8");
            response.sendRedirect("/login");
//            response.getWriter().write("<script>alert('aaaa!');window.history.go(-1);</script>");
        } else /*if (accept.contains("application/json"))*/ {
            response.setContentType("application/json;charset=utf-8");
            Result result = new Result();
            result.setCode(201);
            result.setDescription("aaaa");
            response.getWriter().write(JsonUtil.objectToJson(result));
        }
    }
}
