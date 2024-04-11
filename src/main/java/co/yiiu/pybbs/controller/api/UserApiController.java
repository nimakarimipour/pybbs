package co.yiiu.pybbs.controller.api;

import co.yiiu.pybbs.model.OAuthUser;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.service.*;
import co.yiiu.pybbs.util.MyPage;
import co.yiiu.pybbs.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@RestController
@RequestMapping("/api/user")
public class UserApiController extends BaseApiController {

    @Resource
    private IUserService userService;
    @Resource
    private ITopicService topicService;
    @Resource
    private ICommentService commentService;
    @Resource
    private ICollectService collectService;
    @Resource
    private IOAuthUserService oAuthUserService;

    // aaaaaaa
    @GetMapping("/{username}")
    public Result profile(@PathVariable String username) {
        // aaaaaaaa
        User user = userService.selectByUsername(username);
        // aaoauthaaaaaaa
        List<OAuthUser> oAuthUsers = oAuthUserService.selectByUserId(user.getId());
        // aaaaaaa
        MyPage<Map<String, Object>> topics = topicService.selectByUserId(user.getId(), 1, 10);
        // aaaaaaaaa
        MyPage<Map<String, Object>> comments = commentService.selectByUserId(user.getId(), 1, 10);
        // aaaaaaaaaa
        Integer collectCount = collectService.countByUserId(user.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("oAuthUsers", oAuthUsers);
        map.put("topics", topics);
        map.put("comments", comments);
        map.put("collectCount", collectCount);
        return success(map);
    }

    // aaaaaaa
    @GetMapping("/{username}/topics")
    public Result topics(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo) {
        // aaaaaaaa
        User user = userService.selectByUsername(username);
        // aaaaaaa
        MyPage<Map<String, Object>> topics = topicService.selectByUserId(user.getId(), pageNo, null);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("topics", topics);
        return success(map);
    }

    // aaaaaa
    @GetMapping("/{username}/comments")
    public Result comments(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo) {
        // aaaaaaaa
        User user = userService.selectByUsername(username);
        // aaaaaaaaa
        MyPage<Map<String, Object>> comments = commentService.selectByUserId(user.getId(), pageNo, null);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("comments", comments);
        return success(map);
    }

    // aaaaaaa
    @GetMapping("/{username}/collects")
    public Result collects(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo) {
        // aaaaaaaa
        User user = userService.selectByUsername(username);
        // aaaaaaaaa
        MyPage<Map<String, Object>> collects = collectService.selectByUserId(user.getId(), pageNo, null);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("collects", collects);
        return success(map);
    }
}
