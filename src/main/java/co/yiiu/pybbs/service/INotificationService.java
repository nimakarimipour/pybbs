package co.yiiu.pybbs.service;

import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface INotificationService {
    // aaaa
    List<Map<String, Object>> selectByUserId(Integer userId, Boolean read, Integer limit);

    void markRead(Integer userId);

    // aaaaaaaa
    long countNotRead(Integer userId);

    void deleteByTopicId(Integer topicId);

    void deleteByUserId(Integer userId);

    void insert(Integer userId, Integer targetUserId, Integer topicId, String action, String content);
}
