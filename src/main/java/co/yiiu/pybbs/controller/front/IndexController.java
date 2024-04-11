package co.yiiu.pybbs.controller.front;

import co.yiiu.pybbs.model.Code;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.service.ICodeService;
import co.yiiu.pybbs.service.ISystemConfigService;
import co.yiiu.pybbs.service.IUserService;
import co.yiiu.pybbs.util.CookieUtil;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Controller
public class IndexController extends BaseController {

    private Logger log = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private CookieUtil cookieUtil;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private IUserService userService;
    @Resource
    private ICodeService codeService;

    // aa
    @GetMapping({"/", "/index", "/index.html"})
    public String index(@RequestParam(defaultValue = "all") String tab, @RequestParam(defaultValue = "1") Integer
            pageNo, Boolean active, Model model) {
        model.addAttribute("tab", tab);
        model.addAttribute("active", active);
        model.addAttribute("pageNo", pageNo);

        return render("index");
    }

    @GetMapping("/top100")
    public String top100() {
        return render("top100");
    }

    @GetMapping("/settings")
    public String settings(HttpSession session, Model model) {
        // aaaa，aaaaaaa
        User user = userService.selectById(getUser().getId());
        model.addAttribute("user", user);
        return render("user/settings");
    }

    @GetMapping("/tags")
    public String tags(@RequestParam(defaultValue = "1") Integer pageNo, Model model) {
        model.addAttribute("pageNo", pageNo);
        return render("tag/tags");
    }

    // aa
    @GetMapping("/login")
    public String login() {
        User user = getUser();
        if (user != null) return redirect("/");
        return render("login");
    }

    // aa
    @GetMapping("/register")
    public String register() {
        User user = getUser();
        if (user != null) return redirect("/");
        return render("register");
    }

    // aa
    @GetMapping("/notifications")
    public String notifications() {
        return render("notifications");
    }

    // aa
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        User user = getUser();
        if (user != null) {
            // aaredisaaaa
            userService.delRedisUser(user);
            // aasessionaaaaaa
            session.removeAttribute("_user");
            // aacookieaaaatoken
            cookieUtil.clearCookie(systemConfigService.selectAllConfig().get("cookie_name").toString());
        }
        return redirect("/");
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam String keyword, Model model) {
        model.addAttribute("pageNo", pageNo);
//        model.addAttribute("keyword", SecurityUtil.sanitizeInput(keyword));
        keyword = Jsoup.clean(keyword, Whitelist.basic());
        model.addAttribute("keyword", keyword.replace("\"", "").replace("'", ""));
        return render("search");
    }

    // aaaa
    @GetMapping("changeLanguage")
    public String changeLanguage(String lang, HttpSession session, HttpServletRequest request) {
        String referer = request.getHeader("referer");
        if ("zh".equals(lang)) {
            session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.SIMPLIFIED_CHINESE);
        } else if ("en".equals(lang)) {
            session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.US);
        }
        return StringUtils.isEmpty(referer) ? redirect("/") : redirect(referer);
    }

    // aaaa
    @GetMapping("/active")
    public String active(String email, String code) {
        Assert.notNull(email, "aaaaaaaa");
        Assert.notNull(code, "aaaaaaa");
        User user = getUser();
        if (user == null) {
            user = userService.selectByEmail(email);
        } else {
            Assert.isTrue(email.equals(user.getEmail()), "aaaaaaaaaaaaaaaaaaaa");
        }
        Assert.notNull(user, "aaaaaaaaaaa，aaaa");
        Code code1 = codeService.validateCode(user.getId(), email, null, code);
        Assert.notNull(code1, "aaaaaaaaaaaaa");
        // acodeaaaaaaa
        code1.setUsed(true);
        codeService.update(code1);
        // aaaaaaaaa
        user.setActive(true);
        userService.update(user);
        return redirect("/?active=true");
    }
}
