package co.yiiu.pybbs.controller.front;

import co.yiiu.pybbs.model.Collect;
import co.yiiu.pybbs.model.Tag;
import co.yiiu.pybbs.model.Topic;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.service.ICollectService;
import co.yiiu.pybbs.service.ITagService;
import co.yiiu.pybbs.service.ITopicService;
import co.yiiu.pybbs.service.IUserService;
import co.yiiu.pybbs.util.IpUtil;
import co.yiiu.pybbs.util.MyPage;
import co.yiiu.pybbs.util.SensitiveWordUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Controller
@RequestMapping("/topic")
public class TopicController extends BaseController {

    @Resource
    private ITopicService topicService;
    @Resource
    private ITagService tagService;
    @Resource
    private IUserService userService;
    @Resource
    private ICollectService collectService;

    // aaaa
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model, HttpServletRequest request) {
        // aaaaaa
        Topic topic = topicService.selectById(id);
        Assert.notNull(topic, "aaaaa");
        // aaaaaaaaa
        List<Tag> tags = tagService.selectByTopicId(id);
        // aaaaaaaaa
        User topicUser = userService.selectById(topic.getUserId());
        // aaaaaaaaa
        List<Collect> collects = collectService.selectByTopicId(id);
        // aaaaaaa，aaaaaaaaaaaaa
        if (getUser() != null) {
            Collect collect = collectService.selectByTopicIdAndUserId(id, getUser().getId());
            model.addAttribute("collect", collect);
        }
        // aaaaa+1
        String ip = IpUtil.getIpAddr(request);
        ip = ip.replace(":", "_").replace(".", "_");
        topic = topicService.updateViewCount(topic, ip);

        // aaaaaaa
        topic.setContent(SensitiveWordUtil.replaceSensitiveWord(topic.getContent(), "*", SensitiveWordUtil.MinMatchType));

        model.addAttribute("topic", topic);
        model.addAttribute("tags", tags);
        model.addAttribute("topicUser", topicUser);
        model.addAttribute("collects", collects);
        return render("topic/detail");
    }

    @GetMapping("/create")
    public String create(String tag, Model model) {
        Tag tag1 = tagService.selectByName(tag);
        if (tag1 != null) model.addAttribute("tag", tag);
        return render("topic/create");
    }

    // aaaa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Topic topic = topicService.selectById(id);
        Assert.isTrue(topic.getUserId().equals(getUser().getId()), "aaaaaaaaaaaaaa？");
        // aaaaaaa
        List<Tag> tagList = tagService.selectByTopicId(id);
        // aaaaaaaaaaaaaaa
        String tags = StringUtils.collectionToCommaDelimitedString(tagList.stream().map(Tag::getName).collect(Collectors
                .toList()));

        model.addAttribute("topic", topic);
        model.addAttribute("tags", tags);
        return render("topic/edit");
    }

    @GetMapping("/tag/{name}")
    public String tag(@PathVariable String name, @RequestParam(defaultValue = "1") Integer pageNo, Model model) {
        Tag tag = tagService.selectByName(name);
        Assert.notNull(tag, "aaaaa");
        // aaaaaaaaa
        MyPage<Map<String, Object>> iPage = tagService.selectTopicByTagId(tag.getId(), pageNo);
        model.addAttribute("tag", tag);
        model.addAttribute("page", iPage);
        return render("tag/tag");
    }
}
