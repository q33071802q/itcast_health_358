package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.SysUser;
import com.itheima.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class SecurityConfigUserService implements UserDetailsService {


    @Reference
    UserService userService;

    /***
     * 根据用户载入用户信息
     *  1. 根据用户名从数据获取用户信息，包括角色和权限
     *  2. 封装UserDetails并返回
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1. 根据用户名从数据获取用户信息，包括角色和权限
        SysUser sysUser = userService.findByUsername(username);
        if(sysUser != null){
            //创建权限集合
            List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

            for (Role role : sysUser.getRoles()) {
                for (Permission permission : role.getPermissions()) {
                    //创建权限对象，添加到集合中
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission.getKeyword());
                    authorityList.add(authority);
                }
            }
            //2. 封装UserDetails并返回
            UserDetails userDetails = new User(sysUser.getUsername(), sysUser.getPassword() ,authorityList  );

            return userDetails;
        }
        return null;
    }
}
