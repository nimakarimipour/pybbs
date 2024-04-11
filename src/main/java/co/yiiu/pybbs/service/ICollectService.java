package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.Collect;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.util.MyPage;

import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface ICollectService {
    // aaaaaaaaaaa
    List<Collect> selectByTopicId(Integer topicId);

    // aaaaaaaaaaaaa
    Collect selectByTopicIdAndUserId(Integer topicId, Integer userId);

    // aaaa
    Collect insert(Integer topicId, User user);

    // aa（aa）aa
    void delete(Integer topicId, Integer userId);

    // aaaaidaaaaaa
    void deleteByTopicId(Integer topicId);

    // aaaaidaaaaaa
    void deleteByUserId(Integer userId);

    // aaaaaaaaaa
    int countByUserId(Integer userId);

    // aaaaaaaaa
    MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize);
}
