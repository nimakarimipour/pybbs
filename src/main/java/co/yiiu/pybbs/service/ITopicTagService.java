package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.Tag;
import co.yiiu.pybbs.model.TopicTag;

import java.util.List;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface ITopicTagService {
    List<TopicTag> selectByTopicId(Integer topicId);

    List<TopicTag> selectByTagId(Integer tagId);

    void insertTopicTag(Integer topicId, List<Tag> tagList);

    // aaaaaaaaaaaaa
    void deleteByTopicId(Integer id);
}
