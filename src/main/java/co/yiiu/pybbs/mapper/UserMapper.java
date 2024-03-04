package co.yiiu.pybbs.mapper;

import co.yiiu.pybbs.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.ucr.cs.riple.taint.ucrtainting.qual.RUntainted;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Mapper
public interface UserMapper extends BaseMapper<@RUntainted User> {
    int countToday();
}
