package com.atguigu.springboot.mapper;

import com.atguigu.springboot.bean.MBPUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MBPUserMapper extends BaseMapper<MBPUser> {
}
