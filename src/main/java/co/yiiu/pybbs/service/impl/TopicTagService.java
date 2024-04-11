package co.yiiu.pybbs.service.impl;

import co.yiiu.pybbs.mapper.TopicTagMapper;
import co.yiiu.pybbs.model.Tag;
import co.yiiu.pybbs.model.TopicTag;
import co.yiiu.pybbs.service.ITopicTagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Service
@Transactional
public class TopicTagService implements ITopicTagService {

    @Resource
    private TopicTagMapper topicTagMapper;

    @Override
    public List<TopicTag> selectByTopicId(Integer topicId) {
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTopicId, topicId);
        return topicTagMapper.selectList(wrapper);
    }

    @Override
    public List<TopicTag> selectByTagId(Integer tagId) {
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTagId, tagId);
        return topicTagMapper.selectList(wrapper);
    }

    @Override
    public void insertTopicTag(Integer topicId, List<Tag> tagList) {
        // aaatopicIdaaaaaaa
        this.deleteByTopicId(topicId);
        // aaaaaaaa
        tagList.forEach(tag -> {
            TopicTag topicTag = new TopicTag();
            topicTag.setTopicId(topicId);
            topicTag.setTagId(tag.getId());
            topicTagMapper.insert(topicTag);
        });
    }

    // aaaaaaaaaaaaa
    @Override
    public void deleteByTopicId(Integer id) {
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTopicId, id);
        topicTagMapper.delete(wrapper);
    }
}
