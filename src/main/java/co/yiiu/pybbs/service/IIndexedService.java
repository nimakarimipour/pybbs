package co.yiiu.pybbs.service;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface IIndexedService {

    // aaaaaa
    void indexAllTopic();

    // aaaa
    void indexTopic(String id, String title, String content);

    // aaaaaa
    void deleteTopicIndex(String id);

    // aaaaaaaa
    void batchDeleteIndex();

}
