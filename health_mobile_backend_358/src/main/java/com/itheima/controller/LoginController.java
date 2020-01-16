package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.constant.RedisConst;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    MemberService memberService;

    @Autowired
    JedisPool jedisPool;

    /**
     * 登录操作
     *  步骤：
     *      1. 获取参数中和redis中的验证码，校验
     *          如果校验成功：自动注册为会员
     *          如果校验失败： 返回登录失败
     * @param map
     * @return
     */
    @RequestMapping("/check")
    public Result check(@RequestBody Map<String, String> map){
        //获取参数中验证码
        String validateCodeInParam = map.get("validateCode");
        //获取手机号码
        String telephone = map.get("telephone");
        //获取redis中的验证码
        String validateCodeInRedis = jedisPool.getResource().get(RedisConst.SENDTYPE_LOGIN + "-" + telephone);
        //校验验证码
        if(!(validateCodeInParam != null && validateCodeInParam.equals(validateCodeInRedis))){
            return new Result(false, MessageConst.VALIDATECODE_ERROR);
        }
        //判断是否是会员
        Member member = memberService.findByTelephone(telephone);
        //如果不是会员，自动注册为会员
        if(member == null){
            //自动注册
            member = new Member();
            //设置会员信息
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            //注册
            memberService.add(member);
        }

        return new Result(true,MessageConst.LOGIN_SUCCESS);
    }
}
