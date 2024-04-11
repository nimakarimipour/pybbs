package co.yiiu.pybbs.controller.admin;

import co.yiiu.pybbs.model.AdminUser;
import co.yiiu.pybbs.service.IAdminUserService;
import co.yiiu.pybbs.service.IRoleService;
import co.yiiu.pybbs.util.bcrypt.BCryptPasswordEncoder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Controller
@RequestMapping("/admin/admin_user")
public class AdminUserAdminController extends BaseAdminController {

    @Resource
    private IAdminUserService adminUserService;
    @Resource
    private IRoleService roleService;

    @RequiresPermissions("admin_user:list")
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("adminUsers", adminUserService.selectAll());
        return "admin/admin_user/list";
    }

    @RequiresPermissions("admin_user:add")
    @GetMapping("/add")
    public String add(Model model) {
        // aaaaaaa
        model.addAttribute("roles", roleService.selectAll());
        return "admin/admin_user/add";
    }

    @RequiresPermissions("admin_user:add")
    @PostMapping("/add")
    public String save(AdminUser adminUser) {
        adminUser.setInTime(new Date());
        adminUser.setPassword(new BCryptPasswordEncoder().encode(adminUser.getPassword()));
        adminUserService.insert(adminUser);
        return redirect("/admin/admin_user/list");
    }

    @RequiresPermissions("admin_user:edit")
    @GetMapping("/edit")
    public String edit(Integer id, Model model) {
        AdminUser adminUser = getAdminUser();
        //    Assert.isTrue(adminUser.getId().equals(id), "aaaaaaaaaaaaaaaa？");
        // aaaaaaa
        model.addAttribute("roles", roleService.selectAll());
        model.addAttribute("adminUser", adminUserService.selectById(id));
        return "admin/admin_user/edit";
    }

    @RequiresPermissions("admin_user:edit")
    @PostMapping("/edit")
    public String edit(AdminUser adminUser) {
        AdminUser _adminUser = getAdminUser();
        //    Assert.isTrue(_adminUser.getId().equals(adminUser.getId()), "aaaaaaaaaaaaaaaa？");
        if (StringUtils.isEmpty(adminUser.getPassword())) {
            adminUser.setPassword(null);
        } else {
            adminUser.setPassword(new BCryptPasswordEncoder().encode(adminUser.getPassword()));
        }
        adminUserService.update(adminUser);
        return redirect("/admin/admin_user/list");
    }

    @RequiresPermissions("admin_user:delete")
    @GetMapping("/delete")
    public String delete(Integer id) {
        adminUserService.delete(id);
        return redirect("/admin/admin_user/list");
    }
}
