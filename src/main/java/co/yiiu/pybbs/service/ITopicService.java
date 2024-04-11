package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.Topic;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.util.MyPage;

import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface ITopicService {
    // aa
    MyPage<Map<String, Object>> search(Integer pageNo, Integer pageSize, String keyword);

    // aaaaaa
    MyPage<Map<String, Object>> selectAll(Integer pageNo, String tab);

    // aaaaaaaaaaa
    List<Topic> selectAuthorOtherTopic(Integer userId, Integer topicId, Integer limit);

    // aaaaaaa
    MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize);

    // aaaa
    Topic insert(String title, String content, String tags, User user);

    // aaidaaaa
    Topic selectById(Integer id);

    // aatitleaaaa，aaaaaa
    Topic selectByTitle(String title);

    // aaaaaaaa
    Topic updateViewCount(Topic topic, String ip);

    // aaaa
    void update(Topic topic, String tags);

    // aaaa
    void delete(Topic topic);

    // aaaaidaaaa
    void deleteByUserId(Integer userId);

    MyPage<Map<String, Object>> selectAllForAdmin(Integer pageNo, String startDate, String endDate, String username);

    // aaaaaaaaaa
    int countToday();

    int vote(Topic topic, User user);
}
