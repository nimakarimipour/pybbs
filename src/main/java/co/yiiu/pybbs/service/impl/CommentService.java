package co.yiiu.pybbs.service.impl;

import co.yiiu.pybbs.config.service.EmailService;
import co.yiiu.pybbs.config.service.TelegramBotService;
import co.yiiu.pybbs.config.websocket.MyWebSocket;
import co.yiiu.pybbs.mapper.CommentMapper;
import co.yiiu.pybbs.model.Comment;
import co.yiiu.pybbs.model.Topic;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.model.vo.CommentsByTopic;
import co.yiiu.pybbs.service.*;
import co.yiiu.pybbs.util.Message;
import co.yiiu.pybbs.util.MyPage;
import co.yiiu.pybbs.util.SensitiveWordUtil;
import co.yiiu.pybbs.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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
public class CommentService implements ICommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    @Resource
    private CommentMapper commentMapper;
    @Resource
    @Lazy
    private ITopicService topicService;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    @Lazy
    private IUserService userService;
    @Resource
    private INotificationService notificationService;
    @Resource
    private EmailService emailService;
    @Resource
    private TelegramBotService telegramBotService;

    // aaaaidaaaa
    @Override
    public List<CommentsByTopic> selectByTopicId(Integer topicId) {
        List<CommentsByTopic> commentsByTopics = commentMapper.selectByTopicId(topicId);
        // aaaaaaaaa，aaaaaredis
        for (CommentsByTopic commentsByTopic : commentsByTopics) {
            commentsByTopic.setContent(SensitiveWordUtil.replaceSensitiveWord(commentsByTopic.getContent(), "*",
                    SensitiveWordUtil.MinMatchType));
        }
        return commentsByTopics;
    }

    // aaaaaaaaaaaa
    @Override
    public void deleteByTopicId(Integer topicId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Comment::getTopicId, topicId);
        commentMapper.delete(wrapper);
    }

    // aaaaidaaaaaa
    @Override
    public void deleteByUserId(Integer userId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Comment::getUserId, userId);
        commentMapper.delete(wrapper);
    }

    // aaaa
    @Override
    public Comment insert(Comment comment, Topic topic, User user) {
        if (systemConfigService.selectAllConfig().get("comment_need_examine").equals("1")) {
            comment.setStatus(false);// aaa
        } else {
            comment.setStatus(true);// aaaa
        }
        commentMapper.insert(comment);

        // aaaaaa+1
        topic.setCommentCount(topic.getCommentCount() + 1);
        topicService.update(topic, null);

        // aaaaaa
        user.setScore(user.getScore() + Integer.parseInt(systemConfigService.selectAllConfig().get
                ("create_comment_score")));
        userService.update(user);

        // aa
        // aaaaaaaaa
        if (comment.getCommentId() != null) {
            Comment targetComment = this.selectById(comment.getCommentId());
            if (!user.getId().equals(targetComment.getUserId())) {
                notificationService.insert(user.getId(), targetComment.getUserId(), topic.getId(), "REPLY", comment
                        .getContent());

                String emailTitle = "aaaa %s aaaaa %s aaa，aaaaa！";
                // aaaaawebsocket，aaaaaa
                if (systemConfigService.selectAllConfig().get("websocket").equals("1")) {
                    MyWebSocket.emit(targetComment.getUserId(), new Message("notifications", String.format(emailTitle, topic
                            .getTitle(), user.getUsername())));
                    MyWebSocket.emit(targetComment.getUserId(), new Message("notification_notread", 1));
                }
                // aaaaaa
                User targetUser = userService.selectById(targetComment.getUserId());
                if (!StringUtils.isEmpty(targetUser.getEmail()) && targetUser.getEmailNotification()) {
                    String emailContent = "aaaa: %s <br><a href='%s/topic/%s' target='_blank'>aaa</a>";
                    new Thread(() -> emailService.sendEmail(targetUser.getEmail(), String.format(emailTitle, topic.getTitle(),
                            user.getUsername()), String.format(emailContent, comment.getContent(), systemConfigService
                            .selectAllConfig().get("base_url"), topic.getId()))).start();
                }
            }
        }
        // aaaaaaaa
        if (!user.getId().equals(topic.getUserId())) {
            notificationService.insert(user.getId(), topic.getUserId(), topic.getId(), "COMMENT", comment.getContent());
            // aaaaaa
            String emailTitle = "%s aaaaaa %s aaaaa！";
            // aaaaawebsocket，aaaaaa
            if (systemConfigService.selectAllConfig().get("websocket").equals("1")) {
                MyWebSocket.emit(topic.getUserId(), new Message("notifications", String.format(emailTitle, user.getUsername()
                        , topic.getTitle())));
                MyWebSocket.emit(topic.getUserId(), new Message("notification_notread", 1));
            }
            User targetUser = userService.selectById(topic.getUserId());
            if (!StringUtils.isEmpty(targetUser.getEmail()) && targetUser.getEmailNotification()) {
                String emailContent = "aaaa: %s <br><a href='%s/topic/%s' target='_blank'>aaa</a>";
                new Thread(() -> emailService.sendEmail(targetUser.getEmail(), String.format(emailTitle, user.getUsername(),
                        topic.getTitle()), String.format(emailContent, comment.getContent(), systemConfigService.selectAllConfig
                        ().get("base_url"), topic.getId()))).start();
            }
        }

        // aa TODO

        // aaTGaa
        new Thread(() -> {
            String formatMessage;
            String domain = systemConfigService.selectAllConfig().get("base_url");
            if (systemConfigService.selectAllConfig().get("content_style").equals("MD")) {
                formatMessage = String.format("%s aaaaa [%s](%s) aa： %s", user.getUsername(), topic.getTitle(), domain + "/topic/" + topic.getId(), StringUtil.removeSpecialChar(comment.getContent()));
            } else {
                formatMessage = String.format("%s aaaaa <a href=\"%s\">%s</a> aa： %s", user.getUsername(), domain + "/topic/" + topic.getId(), topic.getTitle(), StringUtil.removeSpecialChar(comment.getContent()));
            }
            Integer message_id = telegramBotService.init().sendMessage(formatMessage, true, null);
            Comment newComment = new Comment();
            newComment.setId(comment.getId());
            newComment.setTgMessageId(message_id);
            commentMapper.updateById(newComment);

        }).start();

        return comment;
    }

    @Override
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    @Override
    public Comment selectByTgMessageId(Integer messageId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Comment::getTgMessageId, messageId);
        List<Comment> comments = commentMapper.selectList(wrapper);
        return comments.size() > 0 ? comments.get(0) : null;
    }

    // aaaa
    @Override
    public void update(Comment comment) {
        commentMapper.updateById(comment);
    }

    // aaaaa
    @Override
    public int vote(Comment comment, User user) {
        String upIds = comment.getUpIds();
        // aaaaaidaaaaaaaa
        Set<String> strings = StringUtils.commaDelimitedListToSet(upIds);
        // aaaaaaaidaaaaa，aaaset，aaaaaa，aaaaaaaaaaaida，aaaaaaaaaaaaa
        Integer userScore = user.getScore();
        if (strings.contains(String.valueOf(user.getId()))) { // aaaaaa
            strings.remove(String.valueOf(user.getId()));
            userScore -= Integer.parseInt(systemConfigService.selectAllConfig().get("up_comment_score"));
        } else { // aaaa
            strings.add(String.valueOf(user.getId()));
            userScore += Integer.parseInt(systemConfigService.selectAllConfig().get("up_comment_score"));
        }
        // aaaaidaaaaaaaaaa
        comment.setUpIds(StringUtils.collectionToCommaDelimitedString(strings));
        // aaaa
        this.update(comment);
        // aaaaaa
        user.setScore(userScore);
        userService.update(user);
        return strings.size();
    }

    // aaaa
    @Override
    public void delete(Comment comment) {
        if (comment != null) {
            // aaaaa-1
            Topic topic = topicService.selectById(comment.getTopicId());
            topic.setCommentCount(topic.getCommentCount() - 1);
            topicService.update(topic, null);
            // aaaaaa
            User user = userService.selectById(comment.getUserId());
            user.setScore(user.getScore() - Integer.parseInt(systemConfigService.selectAllConfig().get
                    ("delete_comment_score").toString()));
            userService.update(user);
            // aaaa
            commentMapper.deleteById(comment.getId());
        }
    }

    // aaaaaaa
    @Override
    public MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize) {
        MyPage<Map<String, Object>> iPage = new MyPage<>(pageNo, pageSize == null ? Integer.parseInt(systemConfigService
                .selectAllConfig().get("page_size").toString()) : pageSize);
        MyPage<Map<String, Object>> page = commentMapper.selectByUserId(iPage, userId);
        for (Map<String, Object> map : page.getRecords()) {
            Object content = map.get("content");
            map.put("content", StringUtils.isEmpty(content) ? null : SensitiveWordUtil.replaceSensitiveWord(content
                    .toString(), "*", SensitiveWordUtil.MinMatchType));
        }
        return page;
    }

    // ---------------------------- admin ----------------------------

    @Override
    public MyPage<Map<String, Object>> selectAllForAdmin(Integer pageNo, String startDate, String endDate, String username) {
        MyPage<Map<String, Object>> iPage = new MyPage<>(pageNo, Integer.parseInt((String) systemConfigService.selectAllConfig().get("page_size")));
        return commentMapper.selectAllForAdmin(iPage, startDate, endDate, username);
    }

    // aaaaaaaaaa
    @Override
    public int countToday() {
        return commentMapper.countToday();
    }
}
