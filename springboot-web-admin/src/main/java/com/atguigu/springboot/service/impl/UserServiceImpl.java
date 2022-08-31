package com.atguigu.springboot.service.impl;

import com.atguigu.springboot.bean.MBPUser;
import com.atguigu.springboot.mapper.MBPUserMapper;
import com.atguigu.springboot.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<MBPUserMapper, MBPUser> implements UserService {
}
