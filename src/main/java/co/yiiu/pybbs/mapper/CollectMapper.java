package co.yiiu.pybbs.mapper;

import co.yiiu.pybbs.model.Collect;
import co.yiiu.pybbs.util.MyPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import edu.ucr.cs.riple.taint.ucrtainting.qual.RUntainted;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public interface CollectMapper extends BaseMapper<Collect> {

    MyPage<Map<String, @RUntainted Object>> selectByUserId(MyPage<Map<String, @RUntainted Object>> iPage, @Param("userId") Integer userId);
}
