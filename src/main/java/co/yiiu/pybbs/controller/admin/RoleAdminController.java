package co.yiiu.pybbs.controller.admin;

import co.yiiu.pybbs.model.AdminUser;
import co.yiiu.pybbs.service.IAdminUserService;
import co.yiiu.pybbs.service.IPermissionService;
import co.yiiu.pybbs.service.IRoleService;
import co.yiiu.pybbs.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Controller
@RequestMapping("/admin/role")
public class RoleAdminController extends BaseAdminController {

    @Resource
    private IRoleService roleService;
    @Resource
    private IPermissionService permissionService;
    @Resource
    private IAdminUserService adminUserService;

    @RequiresPermissions("role:list")
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("roles", roleService.selectAll());
        return "admin/role/list";
    }

    @RequiresPermissions("role:add")
    @GetMapping("/add")
    public String add(Model model) {
        // aaaaaaa
        model.addAttribute("permissions", permissionService.selectAll());
        return "admin/role/add";
    }

    @RequiresPermissions("role:add")
    @PostMapping("/add")
    public String save(String name, Integer[] permissionIds) {
        roleService.insert(name, permissionIds);
        return redirect("/admin/role/list");
    }

    @RequiresPermissions("role:edit")
    @GetMapping("/edit")
    public String edit(Integer id, Model model) {
        model.addAttribute("role", roleService.selectById(id));
        // aaaaaaaaaaa
        model.addAttribute("currentPermissions", permissionService.selectByRoleId(id));
        // aaaaaaa
        model.addAttribute("permissions", permissionService.selectAll());
        return "admin/role/edit";
    }

    @RequiresPermissions("role:edit")
    @PostMapping("/edit")
    public String update(Integer id, String name, Integer[] permissionIds) {
        roleService.update(id, name, permissionIds);
        return redirect("/admin/role/list");
    }

    @RequiresPermissions("role:delete")
    @GetMapping("/delete")
    @ResponseBody
    public Result delete(Integer id) {
        // aaaaaaaaaaaaaaa,aaaaaaaaaaaaaaaa，aaaaaaaaaaaa，aaaa，aaaaaa
        // aaaaaaaaaa，aaaaa
        List<AdminUser> adminUsers = adminUserService.selectByRoleId(id);
        if (adminUsers.size() > 0) {
            List<String> usernames = adminUsers.stream().map(AdminUser::getUsername).collect(Collectors.toList());
            String s = StringUtils.collectionToCommaDelimitedString(usernames);
            return error("aaaaaaaaaaaaa：" + s + " aaaaa，aaaaaaa？");
        } else {
            roleService.delete(id);
            return success();
        }
    }
}
