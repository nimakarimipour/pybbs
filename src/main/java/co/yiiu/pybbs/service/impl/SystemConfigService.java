package co.yiiu.pybbs.service.impl;

import co.yiiu.pybbs.mapper.SystemConfigMapper;
import co.yiiu.pybbs.model.SystemConfig;
import co.yiiu.pybbs.service.ISystemConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Service
@Transactional
public class SystemConfigService implements ISystemConfigService {

    @Resource
    private SystemConfigMapper systemConfigMapper;

    private static Map<String, String> SYSTEM_CONFIG;
    private static Map<String, String> SYSTEM_CONFIG_WITHOUT_PASSWORD;

    @Override
    public Map<String, String> selectAllConfig() {
        if (SYSTEM_CONFIG != null) {
            return SYSTEM_CONFIG;
        }
        List<SystemConfig> systemConfigs = systemConfigMapper.selectList(null);
        SYSTEM_CONFIG = systemConfigs.stream()
                .filter(systemConfig -> systemConfig.getPid() != 0)
                .collect(Collectors.toMap(SystemConfig::getKey, SystemConfig::getValue));
        return SYSTEM_CONFIG;
    }

    // aaaaa
    @Override
    public SystemConfig selectByKey(String key) {
        QueryWrapper<SystemConfig> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SystemConfig::getKey, key);
        return systemConfigMapper.selectOne(wrapper);
    }

    @Override
    public Map<String, Object> selectAll() {
        Map<String, Object> map = new LinkedHashMap<>();
        List<SystemConfig> systemConfigs = systemConfigMapper.selectList(null);
        // aaaaaaaaa
        List<SystemConfig> p = systemConfigs.stream().filter(systemConfig -> systemConfig.getPid() == 0).collect
                (Collectors.toList());
        // aaaaaaaaaaaaaaaa
        p.forEach(systemConfig -> {
            List<SystemConfig> collect = systemConfigs.stream()
                    .filter(systemConfig1 -> systemConfig1.getPid().equals(systemConfig.getId()))
                    .collect(Collectors.toList());
            map.put(systemConfig.getDescription(), collect);
        });
        return map;
    }

    // aaaaaaaa，aaaselectAllConfig()aaa
    @Override
    public void update(List<Map<String, String>> list) {
        for (Map<String, String> map : list) {
            String key = map.get("name");
            String value = map.get("value");
            // aaaaaaaa，aaaaa
            String type = this.selectByKey(key).getType();
            if (!StringUtils.isEmpty(type) && type.equals("password") && value.equals("*******")) {
                continue;
            }
            SystemConfig systemConfig = new SystemConfig();
            systemConfig.setKey(key);
            systemConfig.setValue(value);
            QueryWrapper<SystemConfig> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SystemConfig::getKey, systemConfig.getKey());
            systemConfigMapper.update(systemConfig, wrapper);
        }
        // aaSYSTEM_CONFIG
        SYSTEM_CONFIG = null;
        SYSTEM_CONFIG_WITHOUT_PASSWORD = null;
    }

    // aakeyaaaa
    @Override
    public void updateByKey(String key, SystemConfig systemConfig) {
        QueryWrapper<SystemConfig> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SystemConfig::getKey, key);
        systemConfigMapper.update(systemConfig, wrapper);
        // aaSYSTEM_CONFIG
        SYSTEM_CONFIG = null;
        SYSTEM_CONFIG_WITHOUT_PASSWORD = null;
    }

    @Override
    public Map<String, String> selectAllConfigWithoutPassword() {
        if (SYSTEM_CONFIG_WITHOUT_PASSWORD != null) {
            return SYSTEM_CONFIG_WITHOUT_PASSWORD;
        }
        List<SystemConfig> systemConfigs = systemConfigMapper.selectList(null);
        SYSTEM_CONFIG_WITHOUT_PASSWORD = systemConfigs.stream()
                .filter(systemConfig -> systemConfig.getPid() != 0 && !systemConfig.getType().equals("password"))
                .collect(Collectors.toMap(SystemConfig::getKey, SystemConfig::getValue));
        return SYSTEM_CONFIG_WITHOUT_PASSWORD;
    }
}
