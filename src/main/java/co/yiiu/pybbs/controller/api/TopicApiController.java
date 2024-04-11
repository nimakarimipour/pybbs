package co.yiiu.pybbs.controller.api;

import co.yiiu.pybbs.exception.ApiAssert;
import co.yiiu.pybbs.model.Collect;
import co.yiiu.pybbs.model.Tag;
import co.yiiu.pybbs.model.Topic;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.model.vo.CommentsByTopic;
import co.yiiu.pybbs.service.*;
import co.yiiu.pybbs.util.IpUtil;
import co.yiiu.pybbs.util.Result;
import co.yiiu.pybbs.util.SensitiveWordUtil;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@RestController
@RequestMapping("/api/topic")
public class TopicApiController extends BaseApiController {

    @Resource
    private ITopicService topicService;
    @Resource
    private ITagService tagService;
    @Resource
    private ICommentService commentService;
    @Resource
    private IUserService userService;
    @Resource
    private ICollectService collectService;

    // aaaa
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // aaaaaa
        Topic topic = topicService.selectById(id);
        // aaaaaaaaa
        List<Tag> tags = tagService.selectByTopicId(id);
        // aaaaaaa
        List<CommentsByTopic> comments = commentService.selectByTopicId(id);
        // aaaaaaaaa
        User topicUser = userService.selectById(topic.getUserId());
        // aaaaaaaaa
        List<Collect> collects = collectService.selectByTopicId(id);
        // aaaaaaa，aaaaaaaaaaaaa
        User user = getApiUser(false);
        if (user != null) {
            Collect collect = collectService.selectByTopicIdAndUserId(id, user.getId());
            map.put("collect", collect);
        }
        // aaaaa+1
        String ip = IpUtil.getIpAddr(request);
        ip = ip.replace(":", "_").replace(".", "_");
        topic = topicService.updateViewCount(topic, ip);
        topic.setContent(SensitiveWordUtil.replaceSensitiveWord(topic.getContent(), "*", SensitiveWordUtil.MinMatchType));

        map.put("topic", topic);
        map.put("tags", tags);
        map.put("comments", comments);
        map.put("topicUser", topicUser);
        map.put("collects", collects);
        return success(map);
    }

    // aaaa
    @PostMapping
    public Result create(@RequestBody Map<String, String> body) {
        User user = getApiUser();
        ApiAssert.isTrue(user.getActive(), "aaaaaaaaa，aaaaaaaaaaaa");
        String title = body.get("title");
        String content = body.get("content");
        String tag = body.get("tag");
        //    String tags = body.get("tags");
        title = Jsoup.clean(title, Whitelist.basic());
        ApiAssert.notEmpty(title, "aaaaa");
        ApiAssert.isNull(topicService.selectByTitle(title), "aaaaaa");
        //    String[] strings = StringUtils.commaDelimitedListToStringArray(tags);
        //    Set<String> set = StringUtil.removeEmpty(strings);
        //    ApiAssert.notTrue(set.isEmpty() || set.size() > 5, "aaaaaaaaaa5a");
        // aaaa
        // aaatagaaaaaaaaaa
        //    tags = StringUtils.collectionToCommaDelimitedString(set);
        Topic topic = topicService.insert(title, content, tag, user);
        topic.setContent(SensitiveWordUtil.replaceSensitiveWord(topic.getContent(), "*", SensitiveWordUtil.MinMatchType));
        return success(topic);
    }

    // aaaa
    @PutMapping(value = "/{id}")
    public Result edit(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        User user = getApiUser();
        String title = body.get("title");
        String content = body.get("content");
        ApiAssert.notEmpty(title, "aaaaa");
        // aaaa
        Topic topic = topicService.selectById(id);
        ApiAssert.isTrue(topic.getUserId().equals(user.getId()), "aaaaaaaaaaaaaa？");
        topic.setTitle(Jsoup.clean(title, Whitelist.none().addTags("video")));
        topic.setContent(content);
        topic.setModifyTime(new Date());
        topicService.update(topic, null);
        topic.setContent(SensitiveWordUtil.replaceSensitiveWord(topic.getContent(), "*", SensitiveWordUtil.MinMatchType));
        return success(topic);
    }

    // aaaa
    @DeleteMapping("{id}")
    public Result delete(@PathVariable Integer id) {
        User user = getApiUser();
        Topic topic = topicService.selectById(id);
        ApiAssert.isTrue(topic.getUserId().equals(user.getId()), "aaaaaaaaaaaaaa？");
        topicService.delete(topic);
        return success();
    }

    @GetMapping("/{id}/vote")
    public Result vote(@PathVariable Integer id) {
        User user = getApiUser();
        Topic topic = topicService.selectById(id);
        ApiAssert.notNull(topic, "aaaaaaaaaaaa");
        ApiAssert.notTrue(topic.getUserId().equals(user.getId()), "aaaaaaa，aaaa！！");
        int voteCount = topicService.vote(topic, getApiUser());
        return success(voteCount);
    }
}
