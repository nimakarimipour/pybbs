package co.yiiu.pybbs.controller.admin;

import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.service.IUserService;
import co.yiiu.pybbs.util.Result;
import co.yiiu.pybbs.util.StringUtil;
import co.yiiu.pybbs.util.bcrypt.BCryptPasswordEncoder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Controller
@RequestMapping("/admin/user")
public class UserAdminController extends BaseAdminController {

    @Resource
    private IUserService userService;

    // aaaaaa
    @RequiresPermissions("user:list")
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNo, String username, Model model) {
        if (username != null) username = username.replace("\"", "").replace("'", "");
//        username= SecurityUtil.sanitizeInput(username);
        IPage<User> iPage = userService.selectAll(pageNo, username);
        model.addAttribute("page", iPage);
        model.addAttribute("username", username);
        return "admin/user/list";
    }

    // aaaa
    @RequiresPermissions("user:edit")
    @GetMapping("/edit")
    public String edit(Integer id, Model model) {
        model.addAttribute("user", userService.selectByIdNoCatch(id));
        return "admin/user/edit";
    }

    // aaaaaaaaaa
    @RequiresPermissions("user:edit")
    @PostMapping("/edit")
    @ResponseBody
    public Result update(User user) {
        // aaaaaaa，aaaaaaaa
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        // aaaaaaaaaaaa，useraaaaaaaanull，aaaaafalse
        if (user.getEmailNotification() == null) user.setEmailNotification(false);
        userService.update(user);
        return success();
    }

    // aaaa
    @RequiresPermissions("user:delete")
    @GetMapping("/delete")
    @ResponseBody
    public Result delete(Integer id) {
        userService.deleteUser(id);
        return success();
    }

    // aatoken
    @RequiresPermissions("user:refresh_token")
    @GetMapping("/refreshToken")
    @ResponseBody
    public Result refreshToken(Integer id) {
        User user = userService.selectByIdNoCatch(id);
        user.setToken(StringUtil.uuid());
        userService.update(user);
        return success(user.getToken());
    }
}
