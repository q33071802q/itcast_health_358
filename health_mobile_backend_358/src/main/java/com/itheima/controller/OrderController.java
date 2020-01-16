package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.constant.RedisConst;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;

    /**
     * 步骤：
     *      1. 校验验证码，如果正确，提交，如果错误，返回验证码输出错误
     *      2. 设置预约方式
     *      3. 调用service，添加预约信息
     *      4. 预约成功后，发送预约成功的通知短信给用户
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String,String> map){
        //获取参数中的手机号码
        String telephone = map.get("telephone");

        //1. 校验验证码，如果正确，提交，如果错误，返回验证码输出错误
        //获取redis的验证码
        String validateCodeInRedis = jedisPool.getResource().get(RedisConst.SENDTYPE_ORDER + "-" + telephone);
        //获取参数中的验证码
        String validateCodeInParam = map.get("validateCode");

        if(!(validateCodeInParam != null && validateCodeInParam.equals(validateCodeInRedis))){
            return new Result(false, MessageConst.VALIDATECODE_ERROR);
        }
        //2. 设置预约方式
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        //3. 调用service，添加预约信息
        Result result = null;
        try {
            result = orderService.addOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(result.isFlag()){
            //4. 预约成功后，发送预约成功的通知短信给用户
        }

        return result;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map<String,Object> map = orderService.findById(id);
            Date date = (Date) map.get("orderDate");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String orderDate = sdf.format(date);
            map.put("orderDate", orderDate);

            return new Result(true,MessageConst.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.QUERY_ORDER_FAIL);
        }
    }
}
