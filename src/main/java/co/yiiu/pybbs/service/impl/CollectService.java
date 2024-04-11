package co.yiiu.pybbs.service.impl;

import co.yiiu.pybbs.config.service.EmailService;
import co.yiiu.pybbs.config.websocket.MyWebSocket;
import co.yiiu.pybbs.mapper.CollectMapper;
import co.yiiu.pybbs.model.Collect;
import co.yiiu.pybbs.model.Topic;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.service.*;
import co.yiiu.pybbs.util.Message;
import co.yiiu.pybbs.util.MyPage;
import co.yiiu.pybbs.util.SensitiveWordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Service
@Transactional
public class CollectService implements ICollectService {

    @Resource
    private CollectMapper collectMapper;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private ITagService tagService;
    @Resource
    @Lazy
    private ITopicService topicService;
    @Resource
    private INotificationService notificationService;
    @Resource
    private EmailService emailService;
    @Resource
    @Lazy
    private IUserService userService;

    // aaaaaaaaaaa
    @Override
    public List<Collect> selectByTopicId(Integer topicId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getTopicId, topicId);
        return collectMapper.selectList(wrapper);
    }

    // aaaaaaaaaaaaa
    @Override
    public Collect selectByTopicIdAndUserId(Integer topicId, Integer userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getTopicId, topicId).eq(Collect::getUserId, userId);
        List<Collect> collects = collectMapper.selectList(wrapper);
        if (collects.size() > 0) return collects.get(0);
        return null;
    }

    // aaaa
    @Override
    public Collect insert(Integer topicId, User user) {
        Collect collect = new Collect();
        collect.setTopicId(topicId);
        collect.setUserId(user.getId());
        collect.setInTime(new Date());
        collectMapper.insert(collect);

        // aa
        Topic topic = topicService.selectById(topicId);
        topic.setCollectCount(topic.getCollectCount() + 1);
        topicService.update(topic, null);
        // aaaaaaaaaaa
        if (!user.getId().equals(topic.getUserId())) {
            notificationService.insert(user.getId(), topic.getUserId(), topicId, "COLLECT", null);
            // aaaaaa
            String emailTitle = "aaaa %s a %s aaa，aaaaa！";
            // aaaaawebsocket，aaaaaa
            if (systemConfigService.selectAllConfig().get("websocket").toString().equals("1")) {
                MyWebSocket.emit(topic.getUserId(), new Message("notifications", String.format(emailTitle, topic.getTitle(),
                        user.getUsername())));
            }
            User targetUser = userService.selectById(topic.getUserId());
            if (!StringUtils.isEmpty(targetUser.getEmail()) && targetUser.getEmailNotification()) {
                String emailContent = "<a href='%s/notifications' target='_blank'>aaa</a>";
                new Thread(() -> emailService.sendEmail(targetUser.getEmail(), String.format(emailTitle, topic.getTitle(),
                        user.getUsername()), String.format(emailContent, systemConfigService.selectAllConfig().get("base_url")
                        .toString()))).start();
            }
        }

        return collect;
    }

    // aa（aa）aa
    @Override
    public void delete(Integer topicId, Integer userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getTopicId, topicId).eq(Collect::getUserId, userId);
        collectMapper.delete(wrapper);
        // aaaaaaacollectCountaaaaaa-1
        Topic topic = topicService.selectById(topicId);
        topic.setCollectCount(topic.getCollectCount() - 1);
        topicService.update(topic, null);
    }

    // aaaaidaaaaaa
    @Override
    public void deleteByTopicId(Integer topicId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getTopicId, topicId);
        collectMapper.delete(wrapper);
    }

    // aaaaidaaaaaa
    @Override
    public void deleteByUserId(Integer userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getUserId, userId);
        collectMapper.delete(wrapper);
    }

    // aaaaaaaaaa
    @Override
    public int countByUserId(Integer userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getUserId, userId);
        return collectMapper.selectCount(wrapper);
    }

    // aaaaaaaaa
    @Override
    public MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize) {
        MyPage<Map<String, Object>> page = new MyPage<>(pageNo, pageSize == null ? Integer.parseInt(systemConfigService
                .selectAllConfig().get("page_size").toString()) : pageSize);
        page = collectMapper.selectByUserId(page, userId);
        for (Map<String, Object> map : page.getRecords()) {
            Object content = map.get("content");
            map.put("content", StringUtils.isEmpty(content) ? null : SensitiveWordUtil.replaceSensitiveWord(content
                    .toString(), "*", SensitiveWordUtil.MinMatchType));
        }
        tagService.selectTagsByTopicId(page);
        return page;
    }
}
