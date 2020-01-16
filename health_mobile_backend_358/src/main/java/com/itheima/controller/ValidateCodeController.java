package com.itheima.controller;

import com.itheima.constant.MessageConst;
import com.itheima.constant.RedisConst;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    JedisPool jedisPool;

    /**
     * 为预约发送验证码
     * 步骤：
     *      1. 生成验证码
     *      2. 发送验证码到 手机
     *      3. 存储验证码到redis
     *
     *
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        try {
            //1. 生成验证码
            Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
            //2. 发送验证码到手机
            SMSUtils.sendShortMessage(telephone, String.valueOf(validateCode));
            //3. 存储验证码到redis
            jedisPool.getResource().setex(RedisConst.SENDTYPE_ORDER + "-" + telephone,5 * 60, String.valueOf(validateCode));
            return new Result(true, MessageConst.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConst.SEND_VALIDATECODE_FAIL);
        }

    }

    /**
     * 为登录发送验证码
     *      步骤：
     *          1. 生成验证码
     *          2. 发送验证码给用户
     *          3. 存储验证码到Redis
     *
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        try {
            // 1. 生成验证码
            Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
            //2. 发送验证码给用户
//            SMSUtils.sendShortMessage(telephone, String.valueOf(validateCode));
            //3. 存储验证码到Redis
            jedisPool.getResource().setex(RedisConst.SENDTYPE_LOGIN + "-" + telephone ,5 * 60, String.valueOf(validateCode));
            return new Result(true,MessageConst.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.SEND_VALIDATECODE_FAIL);
        }
    }
}
