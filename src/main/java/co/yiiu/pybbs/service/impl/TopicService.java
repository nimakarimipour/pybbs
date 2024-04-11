package co.yiiu.pybbs.service.impl;

import co.yiiu.pybbs.mapper.TopicMapper;
import co.yiiu.pybbs.model.Tag;
import co.yiiu.pybbs.model.Topic;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.service.*;
import co.yiiu.pybbs.util.MyPage;
import co.yiiu.pybbs.util.SensitiveWordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Service
@Transactional
public class TopicService implements ITopicService {

    @Resource
    private TopicMapper topicMapper;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private ITopicTagService topicTagService;
    @Resource
    private ITagService tagService;
    @Resource
    @Lazy
    private ICollectService collectService;
    @Resource
    @Lazy
    private ICommentService commentService;
    @Resource
    @Lazy
    private IUserService userService;
    @Resource
    private INotificationService notificationService;
    @Resource
    private IndexedService indexedService;

    @Override
    public MyPage<Map<String, Object>> search(Integer pageNo, Integer pageSize, String keyword) {
        if (pageSize == null)
            pageSize = Integer.parseInt(systemConfigService.selectAllConfig().get("page_size").toString());
        MyPage<Map<String, Object>> page = new MyPage<>(pageNo, pageSize);
        return topicMapper.search(page, keyword);
    }

    @Override
    public MyPage<Map<String, Object>> selectAll(Integer pageNo, String tab) {
        MyPage<Map<String, Object>> page = new MyPage<>(pageNo, Integer.parseInt(systemConfigService.selectAllConfig()
                .get("page_size").toString()));
        page = topicMapper.selectAll(page, tab);
        // aaaaaaa
        tagService.selectTagsByTopicId(page);
        return page;
    }

    // aaaaaaaaaaa
    @Override
    public List<Topic> selectAuthorOtherTopic(Integer userId, Integer topicId, Integer limit) {
        QueryWrapper<Topic> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("in_time");
        if (topicId != null) {
            wrapper.lambda().ne(Topic::getId, topicId);
        }
        if (limit != null) wrapper.last("limit " + limit);
        return topicMapper.selectList(wrapper);
    }

    // aaaaaaa
    @Override
    public MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize) {
        MyPage<Map<String, Object>> iPage = new MyPage<>(pageNo, pageSize == null ? Integer.parseInt(systemConfigService
                .selectAllConfig().get("page_size").toString()) : pageSize);
        MyPage<Map<String, Object>> page = topicMapper.selectByUserId(iPage, userId);
        for (Map<String, Object> map : page.getRecords()) {
            Object content = map.get("content");
            map.put("content", StringUtils.isEmpty(content) ? null : SensitiveWordUtil.replaceSensitiveWord(content
                    .toString(), "*", SensitiveWordUtil.MinMatchType));
        }
        return page;
    }

    // aaaa
    @Override
    public Topic insert(String title, String content, String tags, User user) {
        Topic topic = new Topic();
        topic.setTitle(Jsoup.clean(title, Whitelist.simpleText()));
        topic.setStyle(systemConfigService.selectAllConfig().get("content_style"));
        topic.setContent(content);
        topic.setInTime(new Date());
        topic.setUserId(user.getId());
        topic.setTop(false);
        topic.setGood(false);
        topic.setView(1);
        topic.setCollectCount(0);
        topic.setCommentCount(0);
        topicMapper.insert(topic);
        // aaaaaa
        user.setScore(user.getScore() + Integer.parseInt(systemConfigService.selectAllConfig().get("create_topic_score").toString()));
        userService.update(user);
        if (!StringUtils.isEmpty(tags)) {
            // aaaa
            List<Tag> tagList = tagService.insertTag(Jsoup.clean(tags, Whitelist.none()));
            // aaaaaaaaaa
            topicTagService.insertTopicTag(topic.getId(), tagList);
        }
        // aaaa
        indexedService.indexTopic(String.valueOf(topic.getId()), topic.getTitle(), topic.getContent());
        return topic;
    }

    // aaidaaaa
    @Override
    public Topic selectById(Integer id) {
        return topicMapper.selectById(id);
    }

    // aatitleaaaa，aaaaaa
    @Override
    public Topic selectByTitle(String title) {
        QueryWrapper<Topic> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Topic::getTitle, title);
        return topicMapper.selectOne(wrapper);
    }

    // aaaaaaaa
    @Override
    public Topic updateViewCount(Topic topic, String ip) {
        topic.setView(topic.getView() + 1);
        this.update(topic, null);
        return topic;
    }

    // aaaa
    @Override
    public void update(Topic topic, String tags) {
        topicMapper.updateById(topic);
        // aaaa
        if (!StringUtils.isEmpty(tags)) {
            // aaaaatopicCounta-1
            tagService.reduceTopicCount(topic.getId());
            if (!StringUtils.isEmpty(tags)) {
                // aaaa
                List<Tag> tagList = tagService.insertTag(Jsoup.clean(tags, Whitelist.none()));
                // aaaaaaaaaa
                topicTagService.insertTopicTag(topic.getId(), tagList);
            }
        }
        // aaaa
        indexedService.indexTopic(String.valueOf(topic.getId()), topic.getTitle(), topic.getContent());
    }

    // aaaa
    @Override
    public void delete(Topic topic) {
        Integer id = topic.getId();
        // aaaaaa
        notificationService.deleteByTopicId(id);
        // aaaaaa
        collectService.deleteByTopicId(id);
        // aaaaaaa
        commentService.deleteByTopicId(id);
        // aaaaaaaa topicCount -1
        tagService.reduceTopicCount(id);
        // aaaaaaaaa
        topicTagService.deleteByTopicId(id);
        // aaaaaa
        User user = userService.selectById(topic.getUserId());
        user.setScore(user.getScore() - Integer.parseInt(systemConfigService.selectAllConfig().get("delete_topic_score")
                .toString()));
        userService.update(user);
        // aaaa
        indexedService.deleteTopicIndex(String.valueOf(topic.getId()));
        // aaaaaa
        topicMapper.deleteById(id);
    }

    // aaaaidaaaa
    @Override
    public void deleteByUserId(Integer userId) {
        QueryWrapper<Topic> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Topic::getUserId, userId);
        List<Topic> topics = topicMapper.selectList(wrapper);
        topics.forEach(topic -> {
            // aaaa
            indexedService.deleteTopicIndex(String.valueOf(topic.getId()));
            // aaaaaaaaaa
            commentService.deleteByTopicId(topic.getId());
            // aaaaaaaaaa
            collectService.deleteByTopicId(topic.getId());
            // aaaaaaaaaaaaa
            // aaaaatopicCounta-1
            tagService.reduceTopicCount(topic.getId());
            // aaaaaaaaaaaaaa
            topicTagService.deleteByTopicId(topic.getId());
        });
        //aaaa
        topicMapper.delete(wrapper);
    }

    // ---------------------------- admin ----------------------------

    @Override
    public MyPage<Map<String, Object>> selectAllForAdmin(Integer pageNo, String startDate, String endDate, String
            username) {
        MyPage<Map<String, Object>> iPage = new MyPage<>(pageNo, Integer.parseInt((String) systemConfigService
                .selectAllConfig().get("page_size")));
        return topicMapper.selectAllForAdmin(iPage, startDate, endDate, username);
    }

    // aaaaaaaaaa
    @Override
    public int countToday() {
        return topicMapper.countToday();
    }

    // ---------------------------- api ----------------------------

    @Override
    public int vote(Topic topic, User user) {
        String upIds = topic.getUpIds();
        // aaaaaidaaaaaaaa
        Set<String> strings = StringUtils.commaDelimitedListToSet(upIds);
        // aaaaaaaidaaaaa，aaaset，aaaaaa，aaaaaaaaaaaida，aaaaaaaaaaaaa
        Integer userScore = user.getScore();
        if (strings.contains(String.valueOf(user.getId()))) { // aaaaaa
            strings.remove(String.valueOf(user.getId()));
            userScore -= Integer.parseInt(systemConfigService.selectAllConfig().get("up_topic_score").toString());
        } else { // aaaa
            strings.add(String.valueOf(user.getId()));
            userScore += Integer.parseInt(systemConfigService.selectAllConfig().get("up_topic_score").toString());
        }
        // aaaaidaaaaaaaaaa
        topic.setUpIds(StringUtils.collectionToCommaDelimitedString(strings));
        // aaaa
        this.update(topic, null);
        // aaaaaa
        user.setScore(userScore);
        userService.update(user);
        return strings.size();
    }

}
