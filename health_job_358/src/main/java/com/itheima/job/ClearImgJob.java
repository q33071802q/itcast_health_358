package com.itheima.job;

import com.itheima.constant.RedisConst;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class ClearImgJob {


    @Autowired
    JedisPool jedisPool;

    /**
     * 步骤：
     *      1. 获取垃圾图片的名称（找出两个set集合的差值）
     *      2. 循环
     *          2.1 删除七牛云上的图片
     *          2.2 删除redis中垃圾图片名称
     */
    public void clear(){
        //sdiff : 获取两个set集合的差值
        Set<String> imgNames = jedisPool.getResource().sdiff(RedisConst.SETMEAL_PIC_RESOURCES, RedisConst.SETMEAL_PIC_DB_RESOURCES);

        for (String imgName : imgNames) {
            QiniuUtils.deleteFileFromQiniu(imgName);
            //srem : 移除集合中某一个成员
            jedisPool.getResource().srem(RedisConst.SETMEAL_PIC_RESOURCES, imgName);

            System.out.println("您删除了:" + imgName) ;
        }
    }
}
