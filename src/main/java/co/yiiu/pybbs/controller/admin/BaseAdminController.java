package co.yiiu.pybbs.controller.admin;

import co.yiiu.pybbs.controller.api.BaseApiController;
import co.yiiu.pybbs.model.AdminUser;
import co.yiiu.pybbs.service.IAdminUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public class BaseAdminController extends BaseApiController {

    @Resource
    private IAdminUserService adminUserService;

    // aaaaaacontrolleraaaaaDateaaaaStringaaDateaaaaa
    // aaaaaaaaaa，aaaaaaaaStringaDatea
    //  @InitBinder
    //  public void initBinder(ServletRequestDataBinder binder) {
    //    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //    binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    //  }

    protected AdminUser getAdminUser() {
        Subject subject = SecurityUtils.getSubject();
        String principal = (String) subject.getPrincipal();
        return adminUserService.selectByUsername(principal);
    }

}
