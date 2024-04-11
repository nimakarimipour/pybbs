package co.yiiu.pybbs.controller.api;

import co.yiiu.pybbs.exception.ApiAssert;
import co.yiiu.pybbs.model.Comment;
import co.yiiu.pybbs.model.Topic;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.service.ICommentService;
import co.yiiu.pybbs.service.ISystemConfigService;
import co.yiiu.pybbs.service.ITopicService;
import co.yiiu.pybbs.util.Result;
import co.yiiu.pybbs.util.SensitiveWordUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@RestController
@RequestMapping("/api/comment")
public class CommentApiController extends BaseApiController {

    @Resource
    private ICommentService commentService;
    @Resource
    private ITopicService topicService;
    @Resource
    private ISystemConfigService systemConfigService;

    // aaaa
    @PostMapping
    public Result create(@RequestBody Map<String, String> body) {
        User user = getApiUser();
        ApiAssert.isTrue(user.getActive(), "aaaaaaaaa，aaaaaaaaaaaa");
        String content = body.get("content");
        Integer topicId = StringUtils.isEmpty(body.get("topicId")) ? null : Integer.parseInt(body.get("topicId"));
        Integer commentId = StringUtils.isEmpty(body.get("commentId")) ? null : Integer.parseInt(body.get("commentId"));
        ApiAssert.notEmpty(content, "aaaaaaa");
        ApiAssert.notNull(topicId, "aaIDa？");
        Topic topic = topicService.selectById(topicId);
        ApiAssert.notNull(topic, "aaaaa，aaaaaaaaaa");
        // aacommentaa
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setStyle(systemConfigService.selectAllConfig().get("content_style"));
        comment.setContent(content);
        comment.setInTime(new Date());
        comment.setTopicId(topic.getId());
        comment.setUserId(user.getId());
        comment = commentService.insert(comment, topic, user);
        // aaaaaa
        comment.setContent(SensitiveWordUtil.replaceSensitiveWord(comment.getContent(), "*", SensitiveWordUtil.MinMatchType));
        return success(comment);
    }

    // aaaa
    // aaaaaaaaaaaaaaa，aaaaaaaaaaaaaaaaa，aaaaaaaaaaaaaaaa
    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        User user = getApiUser();
        String content = body.get("content");
        ApiAssert.notNull(id, "aaIDa？");
        ApiAssert.notEmpty(content, "aaaaaaa");
        Comment comment = commentService.selectById(id);
        ApiAssert.notNull(comment, "aaaaaaaaaaaa，aaaaaaaaaaaaa");
        ApiAssert.isTrue(comment.getUserId().equals(user.getId()), "aaaaaaaaaaaaaaa？");
        comment.setContent(content);
        commentService.update(comment);
        comment.setContent(SensitiveWordUtil.replaceSensitiveWord(comment.getContent(), "*", SensitiveWordUtil
                .MinMatchType));
        return success(comment);
    }

    // aaaa
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        User user = getApiUser();
        Comment comment = commentService.selectById(id);
        ApiAssert.notNull(comment, "aaaaaaaaaaaa，aaaaaaaaaaaaa");
        ApiAssert.isTrue(comment.getUserId().equals(user.getId()), "aaaaaaaaaaaaaaa？");
        commentService.delete(comment);
        return success();
    }

    // aaaa
    @GetMapping("/{id}/vote")
    public Result vote(@PathVariable Integer id) {
        User user = getApiUser();
        Comment comment = commentService.selectById(id);
        ApiAssert.notNull(comment, "aaaaaaaaaaaa");
        ApiAssert.notTrue(comment.getUserId().equals(user.getId()), "aaaaaaa，aaaa！！");
        int voteCount = commentService.vote(comment, user);
        return success(voteCount);
    }
}
