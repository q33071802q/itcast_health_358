package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.entity.Result;
import com.itheima.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Reference
    UserService userService;

    @RequestMapping("/doSuccess")
    public String doSuccess(){
        return "redirect:http://localhost:83/pages/main.html";
    }

    @RequestMapping("/doFail")
    public String doFail(){
        return "redirect:http://localhost:83/login.html?message=loginFail";
    }


    /**
     * 获取用户名，并返回
     * 用户信息存储在session中
     * @return
     */
    @RequestMapping("/getUsername")
    @ResponseBody
    public Result getUsername(HttpServletRequest request){
//        HttpSession session = request.getSession();
        //获取session中所有的属性名
        //枚举类型
        //session只有一个属性： SPRING_SECURITY_CONTEXT 对应的对象时： SecurityContext
//        Enumeration attributeNames = session.getAttributeNames();
//        while(attributeNames.hasMoreElements()){
//            System.out.println(attributeNames.nextElement());
//        }
        try {
            //使用帮助类获取安全框架上下文对象
            SecurityContext context = SecurityContextHolder.getContext();
            //获取认证对象
            Authentication authentication = context.getAuthentication();
            //获取重要信息
            Object principal = authentication.getPrincipal();
            //转换为User类型
            User user = (User) principal;
            //获取username
            String username = user.getUsername();
            return  new Result(true, MessageConst.GET_USERNAME_SUCCESS,username);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConst.GET_USERNAME_FAIL);
        }
    }

}
