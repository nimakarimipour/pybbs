package co.yiiu.pybbs.service;

import co.yiiu.pybbs.model.SystemConfig;

import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface ISystemConfigService {
    Map<String, String> selectAllConfig();

    // aaaaa
    SystemConfig selectByKey(String key);

    Map<String, Object> selectAll();

    // aaaaaaaa，aaaselectAllConfig()aaa
    void update(List<Map<String, String>> list);

    // aakeyaaaa
    void updateByKey(String key, SystemConfig systemConfig);

    Map<String, String> selectAllConfigWithoutPassword();
}
