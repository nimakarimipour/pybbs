package co.yiiu.pybbs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public class SystemConfig implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    // aaa
    @TableField("`key`")
    private String key;
    // aaa
    @TableField("`value`")
    private String value;
    // aaaa
    private String description;
    private Integer pid;

    // aaaa，aaaa select, input[type=text,url,number,radio,password,email]
    @TableField("`type`")
    private String type;

    // aaaaaaa，aa radio，select aoption
    @TableField("`option`")
    private String option;

    // aaaaaaaaa
    @TableField("`reboot`")
    private String reboot;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getReboot() {
        return reboot;
    }

    public void setReboot(String reboot) {
        this.reboot = reboot;
    }

    @Override
    public String toString() {
        return "SystemConfig{" + "id=" + id + ", key='" + key + '\'' + ", value='" + value + '\'' + ", description='" +
                description + '\'' + ", pid=" + pid + ", type='" + type + '\'' + ", option='" + option + '\'' + ", reboot='"
                + reboot + '\'' + '}';
    }
}
