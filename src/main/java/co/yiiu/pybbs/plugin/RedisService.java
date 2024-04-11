package co.yiiu.pybbs.plugin;

import co.yiiu.pybbs.config.service.BaseService;
import co.yiiu.pybbs.model.SystemConfig;
import co.yiiu.pybbs.service.ISystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Component
@DependsOn("mybatisPlusConfig")
public class RedisService implements BaseService<JedisPool> {

    @Resource
    private ISystemConfigService systemConfigService;
    private JedisPool jedisPool;
    private final Logger log = LoggerFactory.getLogger(RedisService.class);

    public void setJedis(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPool instance() {
        try {
            if (this.jedisPool != null) return this.jedisPool;
            // aaredisaaa
            // host
            SystemConfig systemConfigHost = systemConfigService.selectByKey("redis_host");
            String host = systemConfigHost.getValue();
            // port
            SystemConfig systemConfigPort = systemConfigService.selectByKey("redis_port");
            String port = systemConfigPort.getValue();
            // password
            SystemConfig systemConfigPassword = systemConfigService.selectByKey("redis_password");
            String password = systemConfigPassword.getValue();
            password = StringUtils.isEmpty(password) ? null : password;
            // database
            SystemConfig systemConfigDatabase = systemConfigService.selectByKey("redis_database");
            String database = systemConfigDatabase.getValue();
            // timeout
            SystemConfig systemConfigTimeout = systemConfigService.selectByKey("redis_timeout");
            String timeout = systemConfigTimeout.getValue();

            if (!this.isRedisConfig()) {
                log.info("redisaaaaaaaaaaa...");
                return null;
            }
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            // aajedisaaaaaaaaaaaa，aaaa 8
            jedisPoolConfig.setMaxIdle(7);
            // aajedisaaaaaaaaaaaa，aaaa 8
            jedisPoolConfig.setMaxTotal(20);
            //aborrow(aa)aajedisaaa，aaaaaavalidateaa；aaatrue，aaaajedisaaaaaaa；
            jedisPoolConfig.setTestOnBorrow(true);
            //return aajedisaaapoola，aaaaaaaaa（ping()）
            jedisPoolConfig.setTestOnReturn(true);
            jedisPool = new JedisPool(jedisPoolConfig, host, Integer.parseInt(port), Integer.parseInt(timeout), password,
                    Integer.parseInt(database));
            log.info("redisaaaaaaaa...");
            return this.jedisPool;
        } catch (Exception e) {
            log.error("aaredisaaaaa，aaaa: {}", e.getMessage());
            return null;
        }
    }

    // aaredisaaaaa
    public boolean isRedisConfig() {
        SystemConfig systemConfigHost = systemConfigService.selectByKey("redis_host");
        String host = systemConfigHost.getValue();
        // port
        SystemConfig systemConfigPort = systemConfigService.selectByKey("redis_port");
        String port = systemConfigPort.getValue();
        // database
        SystemConfig systemConfigDatabase = systemConfigService.selectByKey("redis_database");
        String database = systemConfigDatabase.getValue();
        // timeout
        SystemConfig systemConfigTimeout = systemConfigService.selectByKey("redis_timeout");
        String timeout = systemConfigTimeout.getValue();

        return !StringUtils.isEmpty(host) && !StringUtils.isEmpty(port) && !StringUtils.isEmpty(database) && !StringUtils
                .isEmpty(timeout);
    }

    // aaStringa
    public String getString(String key) {
        JedisPool instance = this.instance();
        if (StringUtils.isEmpty(key) || instance == null) return null;
        Jedis jedis = instance.getResource();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    public void setString(String key, String value) {
        this.setString(key, value, 300); // aaaaaaaaa，aaa5aa
    }

    /**
     * aaaaaaaaaaaaredis，aaaaaa
     *
     * @param key
     * @param value
     * @param expireTime aa a
     */
    public void setString(String key, String value, int expireTime) {
        JedisPool instance = this.instance();
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value) || instance == null) return;
        Jedis jedis = instance.getResource();
        SetParams params = new SetParams();
        params.px(expireTime * 1000);
        jedis.set(key, value, params);
        jedis.close();
    }

    public void delString(String key) {
        JedisPool instance = this.instance();
        if (StringUtils.isEmpty(key) || instance == null) return;
        Jedis jedis = instance.getResource();
        jedis.del(key); // aaaaaa 1
        jedis.close();
    }

    // TODO aaaaaaaaaa list, map aaa

}
