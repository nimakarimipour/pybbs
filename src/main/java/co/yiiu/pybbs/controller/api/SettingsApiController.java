package co.yiiu.pybbs.controller.api;

import co.yiiu.pybbs.exception.ApiAssert;
import co.yiiu.pybbs.model.Code;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.service.ICodeService;
import co.yiiu.pybbs.service.ISystemConfigService;
import co.yiiu.pybbs.service.IUserService;
import co.yiiu.pybbs.util.Result;
import co.yiiu.pybbs.util.StringUtil;
import co.yiiu.pybbs.util.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@RestController
@RequestMapping("/api/settings")
public class SettingsApiController extends BaseApiController {

    @Resource
    private IUserService userService;
    @Resource
    private ICodeService codeService;
    @Resource
    private ISystemConfigService systemConfigService;

    // aaaaaaaa
    @PutMapping
    public Result update(@RequestBody Map<String, String> body, HttpSession session) {
        User user = getApiUser();
        String telegramName = body.get("telegramName");
        String website = body.get("website");
        String bio = body.get("bio");
        Boolean emailNotification = Boolean.parseBoolean(body.get("emailNotification"));
        // aaaaaaaaaaa
        User user1 = userService.selectById(user.getId());
        user1.setTelegramName(telegramName);
        user1.setWebsite(website);
        user1.setBio(bio);
        user1.setEmailNotification(emailNotification);
        userService.update(user1);

        User user2 = getUser();
        if (user2 != null) {
            user2.setBio(bio);
            session.setAttribute("_user", user2);
        }
        return success();
    }

    // aaaaaa
    @GetMapping("/sendActiveEmail")
    public Result sendActiveEmail() {
        User user = getApiUser();
        ApiAssert.notTrue(StringUtils.isEmpty(user.getEmail()), "aaaaaaaaaaa，aaaaaa");
        ApiAssert.notTrue(user.getActive(), "aaaaaaaaaaaaa，aaaaaaaaaa");

        String title = "aaaa%s，aaaaaaaaaa";
        String content = "aaaaaaaa%s，aaaaaa&nbsp;&nbsp;<a href='%s/active?email=%s&code=${code}'>aaaa</a>";

        if (codeService.sendEmail(
                user.getId(),
                user.getEmail(),
                String.format(title, systemConfigService.selectAllConfig().get("base_url").toString()),
                String.format(content, systemConfigService.selectAllConfig().get("name").toString(),
                        systemConfigService.selectAllConfig().get("base_url").toString(),
                        user.getEmail()))) {
            return success();
        } else {
            return error("aaaaaa，aaaaaaaaaaaa");
        }
    }

    // aaaaaaa
    @GetMapping("/sendEmailCode")
    public Result sendEmailCode(String email) {
        User user = getApiUser();
        ApiAssert.notEmpty(email, "aaaaa ");
        ApiAssert.isTrue(StringUtil.check(email, StringUtil.EMAILREGEX), "aaaaaaa");
        User emailUser = userService.selectByEmail(email);
        ApiAssert.isNull(emailUser, "aaaaaaaaaaa，aaaaaaa");
        if (codeService.sendEmail(user.getId(), email, "aaaaaaa", "aaaaaa：<code>${code}</code><br>aa30aaaaa")) {
            return success();
        } else {
            return error("aaaaaa，aaaaaaaaaaaa");
        }
    }

    // aaaaaa
    @PutMapping("/updateEmail")
    public Result updateEmail(@RequestBody Map<String, String> body, HttpSession session) {
        User user = getApiUser();
        String email = body.get("email");
        String code = body.get("code");
        ApiAssert.notEmpty(email, "aaaaa ");
        ApiAssert.isTrue(StringUtil.check(email, StringUtil.EMAILREGEX), "aaaaaaa");
        Code code1 = codeService.validateCode(user.getId(), email, null, code);
        if (code1 == null) return error("aaaaa");
        // acodeaaaaaaa
        code1.setUsed(true);
        codeService.update(code1);
        // aaaaaaaaaaa
        User user1 = userService.selectById(user.getId());
        user1.setEmail(email);
        // aaaaaaaaaaa，aaaaaaaaaaaaaa
        if (!user1.getActive()) user1.setActive(true);
        userService.update(user1);
        // aasessionaaaaaa
        User _user = getUser();
        _user.setEmail(email);
        session.setAttribute("_user", _user);
        return success();
    }

    // aaaa
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map<String, String> body) {
        User user = getApiUser();
        user = userService.selectByIdWithoutCache(user.getId());

        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        ApiAssert.notEmpty(oldPassword, "aaaaaa");
        ApiAssert.notEmpty(newPassword, "aaaaaa");
        ApiAssert.notTrue(oldPassword.equals(newPassword), "aaaaaaaaa？");
        ApiAssert.isTrue(new BCryptPasswordEncoder().matches(oldPassword, user.getPassword()), "aaaaaa");

        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userService.update(user);
        return success();
    }

    // aatoken
    @GetMapping("/refreshToken")
    public Result refreshToken(HttpSession session) {
        User user = getApiUser();
        String token = StringUtil.uuid();
        user.setToken(token);
        userService.update(user);
        // aasessionaaaaaa
        User _user = getUser();
        _user.setToken(token);
        session.setAttribute("_user", _user);
        return success(token);
    }

}
