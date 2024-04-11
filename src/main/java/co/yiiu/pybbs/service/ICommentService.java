package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.Comment;
import co.yiiu.pybbs.model.Topic;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.model.vo.CommentsByTopic;
import co.yiiu.pybbs.util.MyPage;

import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface ICommentService {
    // aaaaidaaaa
    List<CommentsByTopic> selectByTopicId(Integer topicId);

    // aaaaaaaaaaaa
    void deleteByTopicId(Integer topicId);

    // aaaaidaaaaaa
    void deleteByUserId(Integer userId);

    // aaaa
    Comment insert(Comment comment, Topic topic, User user);

    Comment selectById(Integer id);

    Comment selectByTgMessageId(Integer messageId);

    // aaaa
    void update(Comment comment);

    // aaaaa
    int vote(Comment comment, User user);

    // aaaa
    void delete(Comment comment);

    // aaaaaaa
    MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize);

    MyPage<Map<String, Object>> selectAllForAdmin(Integer pageNo, String startDate, String endDate, String username);

    // aaaaaaaaaa
    int countToday();
}
