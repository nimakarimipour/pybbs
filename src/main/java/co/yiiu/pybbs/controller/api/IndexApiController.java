package co.yiiu.pybbs.controller.api;

import co.yiiu.pybbs.exception.ApiAssert;
import co.yiiu.pybbs.model.Code;
import co.yiiu.pybbs.model.Tag;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.service.*;
import co.yiiu.pybbs.util.*;
import co.yiiu.pybbs.util.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@RestController
@RequestMapping("/api")
public class IndexApiController extends BaseApiController {

    @Resource
    private IUserService userService;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private CookieUtil cookieUtil;
    @Resource
    private ITopicService topicService;
    @Resource
    private ITagService tagService;
    @Resource
    private FileUtil fileUtil;
    @Resource
    private ICodeService codeService;

    // aaaa
    @GetMapping({"/", "/index"})
    public Result index(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "all") String
            tab) {
        MyPage<Map<String, Object>> page = topicService.selectAll(pageNo, tab);
        for (Map<String, Object> map : page.getRecords()) {
            Object content = map.get("content");
            map.put("content", StringUtils.isEmpty(content) ? null : SensitiveWordUtil.replaceSensitiveWord(content
                    .toString(), "*", SensitiveWordUtil.MinMatchType));
        }
        return success(page);
    }

    // aaaaaaaaaaaa
    @GetMapping("/tag/{name}")
    public Result topicsByTagName(@RequestParam(defaultValue = "1") Integer pageNo, @PathVariable String name) {
        Tag tag = tagService.selectByName(name);
        if (tag == null) {
            return error("aaaaa");
        } else {
            MyPage<Map<String, Object>> iPage = tagService.selectTopicByTagId(tag.getId(), pageNo);
            Map<String, Object> result = new HashMap<>();
            result.put("tag", tag);
            result.put("page", iPage);
            return success(result);
        }
    }

    // aaaaaaa
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.get("username");
        String password = body.get("password");
        String captcha = body.get("captcha");
        String _captcha = (String) session.getAttribute("_captcha");
        ApiAssert.notTrue(_captcha == null || StringUtils.isEmpty(captcha), "aaaaaa");
        ApiAssert.notTrue(!_captcha.equalsIgnoreCase(captcha), "aaaaaa");
        ApiAssert.notEmpty(username, "aaaaaa");
        ApiAssert.notEmpty(password, "aaaaa");
        User user = userService.selectByUsername(username);
        ApiAssert.notNull(user, "aaaaa");
        ApiAssert.isTrue(new BCryptPasswordEncoder().matches(password, user.getPassword()), "aaaaaaaaa");
        return this.doUserStorage(session, user);
    }

    // aaaaaaa
    @PostMapping("/register")
    public Result register(@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");
        String captcha = body.get("captcha");
        String _captcha = (String) session.getAttribute("_captcha");
        ApiAssert.notTrue(_captcha == null || StringUtils.isEmpty(captcha), "aaaaaa");
        ApiAssert.notTrue(!_captcha.equalsIgnoreCase(captcha), "aaaaaa");
        ApiAssert.notEmpty(username, "aaaaaa");
        ApiAssert.notEmpty(password, "aaaaa");
        ApiAssert.notEmpty(email, "aaaaa");
        ApiAssert.isTrue(StringUtil.check(username, StringUtil.USERNAMEREGEX), "aaaaaaa-z,A-Z,0-9aaa2-16a");
        ApiAssert.isTrue(StringUtil.check(email, StringUtil.EMAILREGEX), "aaaaaaaaaa");
        User user = userService.selectByUsername(username);
        ApiAssert.isNull(user, "aaaaaa");
        User emailUser = userService.selectByEmail(email);
        ApiAssert.isNull(emailUser, "aaaaaaaaaaa，aaaaaaa");
        user = userService.addUser(username, password, null, email, null, null, true);
        return this.doUserStorage(session, user);
    }

    // aaaaaaa
    @GetMapping("/sms_code")
    public Result sms_code(String captcha, String mobile, HttpSession session) {
        String _captcha = (String) session.getAttribute("_captcha");
        ApiAssert.notTrue(_captcha == null || StringUtils.isEmpty(captcha), "aaaaaa");
        ApiAssert.notTrue(!_captcha.equalsIgnoreCase(captcha), "aaaaaa");
        ApiAssert.notEmpty(mobile, "aaaaaa");
        ApiAssert.isTrue(StringUtil.check(mobile, StringUtil.MOBILEREGEX), "aaaaaaaaa");
        boolean b = codeService.sendSms(mobile);
        if (!b) {
            return error("aaaaaaaaaaaaaaaaaa");
        } else {
            return success();
        }
    }

    // aaa+aaaaa
    @PostMapping("/mobile_login")
    public Result mobile_login(@RequestBody Map<String, String> body, HttpSession session) {
        String mobile = body.get("mobile");
        String code = body.get("code");
        String captcha = body.get("captcha");
        String _captcha = (String) session.getAttribute("_captcha");
        ApiAssert.notTrue(_captcha == null || StringUtils.isEmpty(captcha), "aaaaaa");
        ApiAssert.notTrue(!_captcha.equalsIgnoreCase(captcha), "aaaaaa");
        ApiAssert.notEmpty(mobile, "aaaaaa");
        ApiAssert.isTrue(StringUtil.check(mobile, StringUtil.MOBILEREGEX), "aaaaaaaaa");
        ApiAssert.notEmpty(code, "aaaaaaaa");
        Code validateCode = codeService.validateCode(null, null, mobile, code);
        ApiAssert.notTrue(validateCode == null, "aaaaaaa");
        User user = userService.addUserWithMobile(mobile);
        return doUserStorage(session, user);
    }

    // aaaaaaaaaaaaaaaa
    // aaaaaaaaaaaaaaaa
    // @PostMapping("/forget_password")
    //  public Result forget_password(@RequestBody Map<String, String> body, HttpSession session) {
    //    String email = body.get("email");
    //    String captcha = body.get("captcha");
    //    String _captcha = (String) session.getAttribute("_captcha");
    //    ApiAssert.notTrue(_captcha == null || StringUtils.isEmpty(captcha), "aaaaaa");
    //    ApiAssert.notTrue(!_captcha.equalsIgnoreCase(captcha), "aaaaaa");
    //    ApiAssert.notEmpty(email, "aaaaa");
    //    ApiAssert.isTrue(StringUtil.check(email, StringUtil.EMAILREGEX), "aaaaaaaaaa");
    //    emailService.send
    //  }

    // aaaaa，aaaaaaa，aaaaaaaaaaaaaa
    private Result doUserStorage(HttpSession session, User user) {
        // aaaaaasession
        if (session != null) {
            session.setAttribute("_user", user);
            session.removeAttribute("_captcha");
        }
        // aaatokenacookie
        cookieUtil.setCookie(systemConfigService.selectAllConfig().get("cookie_name").toString(), user.getToken());
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", user.getToken());
        return success(map);
    }

    // aaaa
    @GetMapping("/tags")
    public Result tags(@RequestParam(defaultValue = "1") Integer pageNo) {
        return success(tagService.selectAll(pageNo, null, null));
    }

    // aaaa
    @PostMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile[] files, String type, HttpSession session) {
        User user = getApiUser();
        ApiAssert.isTrue(user.getActive(), "aaaaaaaaa，aaaaaaaaaaaa");
        ApiAssert.notEmpty(type, "aaaaaaaaaa");
        Map<String, Object> resultMap = new HashMap<>();
        List<String> urls = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String url;
            MultipartFile file = files[i];
            String suffix = "." + Objects.requireNonNull(file.getContentType()).split("/")[1];
            if (!Arrays.asList(".jpg", ".png", ".gif", ".jpeg", ".mp4").contains(suffix.toLowerCase())) {
                errors.add("a[" + (i + 1) + "]aaaaa: " + "aaaaaaa");
                continue;
            }
            long size = file.getSize();
            // aaaaaaaa，aaaaaaaa
            if (type.equalsIgnoreCase("video")) {
                long uploadVideoSizeLimit = Long.parseLong(systemConfigService.selectAllConfig().get("upload_video_size_limit").toString());
                if (size > uploadVideoSizeLimit * 1024 * 1024) {
                    errors.add("a[" + (i + 1) + "]aaaaa: " + "aaaaa，aaaaaaaa " + uploadVideoSizeLimit + "MB aa");
                    continue;
                }
            } else {
                long uploadImageSizeLimit = Long.parseLong(systemConfigService.selectAllConfig().get("upload_image_size_limit").toString());
                if (size > uploadImageSizeLimit * 1024 * 1024) {
                    errors.add("a[" + (i + 1) + "]aaaaa: " + "aaaaa，aaaaaaaa " + uploadImageSizeLimit + "MB aa");
                    continue;
                }
            }
            if (type.equalsIgnoreCase("avatar")) { // aaaa
                // aaaaaaaaurl
                url = fileUtil.upload(file, "avatar", "avatar/" + user.getUsername());
                if (url != null) {
                    // aaaaaaaaaaa
                    User user1 = userService.selectById(user.getId());
                    user1.setAvatar(url);
                    // aaaaaaaa
                    userService.update(user1);
                    // aaaaaaaaaaasessiona
                    if (session != null) session.setAttribute("_user", user1);
                }
            } else if (type.equalsIgnoreCase("topic")) { // aaaaaa
                url = fileUtil.upload(file, null, "topic/" + user.getUsername());
            } else if (type.equalsIgnoreCase("video")) { // aaaa
                url = fileUtil.upload(file, null, "video/" + user.getUsername());
            } else {
                errors.add("a[" + (i + 1) + "]aaaaa: " + "aaaaaaaaaaaaa");
                continue;
            }
            if (url == null) {
                errors.add("a[" + (i + 1) + "]aaaaa: " + "aaaaaaaaaaaaaaaaaaa");
                continue;
            }
            urls.add(url);
        }
        resultMap.put("urls", urls);
        resultMap.put("errors", errors);
        return success(resultMap);
    }

}
