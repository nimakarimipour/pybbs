package co.yiiu.pybbs.interceptor;

import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.service.ISystemConfigService;
import co.yiiu.pybbs.service.IUserService;
import co.yiiu.pybbs.util.CookieUtil;
import co.yiiu.pybbs.util.HttpUtil;
import co.yiiu.pybbs.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Component
public class CommonInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(CommonInterceptor.class);
    @Resource
    private IUserService userService;
    @Resource
    private CookieUtil cookieUtil;
    @Resource
    private ISystemConfigService systemConfigService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long start = System.currentTimeMillis();
        request.setAttribute("_start", start);

        // aasessionaaaaaa，aaaaa
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("_user");
        if (user == null) {
            // aacookieaatoken，aaaaaaaaaasessiona
            String token = cookieUtil.getCookie(systemConfigService.selectAllConfig().get("cookie_name").toString());
            if (!StringUtils.isEmpty(token)) {
                // aatokenaaaaaaaa
                user = userService.selectByToken(token);
                if (user != null) {
                    // aaaaasession，cookieaaaaaa
                    session.setAttribute("_user", user);
                    cookieUtil.setCookie(systemConfigService.selectAllConfig().get("cookie_name").toString(), user.getToken());
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (!HttpUtil.isApiRequest(request) && modelAndView != null) {
            // TODO aaaaaaaa，aaaaaa，aaaaaaaaaasystem_configaaaaaaaa，aaaaaaaaaaaaaaaa，aaaaaaaaaaaaaa。。
            // aaaaaaaaa，aaaa
            // 2023/3/14 aaaasystemConfigatypeapasswordaaa，aaaaaaaaaaaaa
            modelAndView.addObject("site", systemConfigService.selectAllConfigWithoutPassword());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long start = (long) request.getAttribute("_start");
        String actionName = request.getRequestURI();
        String clientIp = IpUtil.getIpAddr(request);
        StringBuilder logString = new StringBuilder();
        logString.append(clientIp).append("|").append(actionName).append("|");
        Map<String, String[]> params = request.getParameterMap();
        params.forEach((key, value) -> {
            logString.append(key);
            logString.append("=");
            for (String paramString : value) {
                logString.append(paramString);
            }
            logString.append("|");
        });
        long executionTime = System.currentTimeMillis() - start;
        logString.append("excitation=").append(executionTime).append("ms");
        log.info(logString.toString());
    }
}
