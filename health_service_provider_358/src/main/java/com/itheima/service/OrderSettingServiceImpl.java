package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao ;

    /**
     * 步骤:
     *      1. 循环
     *      2.判断该日期是否进行过预约设置
     *          2.1 如果没有，直接添加
     *          2.2 如果存在
     *              2.2.1 修改之前，要判断已预约是否大于可预约
     *              2.2.2 执行修改
     *
     * @param orderSettingList
     */
    @Override
    public void addOrderSettings(List<OrderSetting> orderSettingList) {
        if(orderSettingList != null && orderSettingList.size() > 0){
            for (OrderSetting orderSetting : orderSettingList) {
                //2.判断该日期是否进行过预约设置
                OrderSetting orderSettingDb = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
                //2.1 如果没有，直接添加
                if(orderSettingDb == null){
                    orderSettingDao.add(orderSetting);
                }else{
                    //2.2 如果存在
                    //2.2.1 修改之前，要判断已预约是否大于可预约
                    //orderSettingDb.getReservations()； 数据库中的已预约人数
                    //orderSetting.getNumber() :  即将要修改成的可预约人数
                    if(orderSettingDb.getReservations() > orderSetting.getNumber()){
                        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
                        throw new RuntimeException("[ "+sdf.format(orderSetting.getOrderDate()) + " ]已预约大于可预约，不能设置！！");
                    }else{
                        //2.2.2 执行修改
                        orderSettingDao.edit(orderSetting);
                    }
                }
            }
        }
    }

    /**
     * month = 2019-11
     *       2019-11-01 ---  2019-11-31
     * @param month
     * @return
     */
    @Override
    public List<OrderSetting> findByMonth(String month) {
        String startDate = month + "-01";
        String endDate = month + "-31";

        Map<String,String> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        return orderSettingDao.findByBetweenDate(map);
    }
}
