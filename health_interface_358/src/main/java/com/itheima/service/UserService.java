package com.itheima.service;

import com.itheima.pojo.SysUser;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface UserService {
    /**
     * 根据用户名载入用户信息, 包含角色和权限
     *
     * @param username
     * @return
     */
    SysUser findByUsername(String username);
}
