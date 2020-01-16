package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.UserDao;
import com.itheima.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    /**
     *
     * @param username
     * @return
     */
    @Override
    public SysUser findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
