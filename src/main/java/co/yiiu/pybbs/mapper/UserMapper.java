package co.yiiu.pybbs.mapper;

import co.yiiu.pybbs.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Mapper
public interface UserMapper<T> extends BaseMapper<T> {
    int countToday();
}
