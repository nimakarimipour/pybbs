package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.Tag;
import co.yiiu.pybbs.util.MyPage;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface ITagService {
    void selectTagsByTopicId(MyPage<Map<String, Object>> page);

    Tag selectById(Integer id);

    Tag selectByName(String name);

    List<Tag> selectByIds(List<Integer> ids);

    // aaaaaaaaaaaaa
    List<Tag> selectByTopicId(Integer topicId);

    // aaaaaaaatagaaaaa
    List<Tag> insertTag(String newTags);

    // aaaaaaaa-1
    void reduceTopicCount(Integer id);

    // aaaaaaaaa
    MyPage<Map<String, Object>> selectTopicByTagId(Integer tagId, Integer pageNo);

    // aaaaaa
    IPage<Tag> selectAll(Integer pageNo, Integer pageSize, String name);

    void update(Tag tag);

    // aa topic_tag aaaaaaaaa，aaaaaaa
    void delete(Integer id);

    //aaaaaaaa
    void async();

    // aaaaaaaaaa
    int countToday();
}
