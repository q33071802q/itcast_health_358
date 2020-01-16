package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map; /**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface OrderSettingDao {
    OrderSetting findByOrderDate(Date orderDate);

    void edit(OrderSetting orderSetting);

    List<OrderSetting> findByBetweenDate(Map<String, String> map);

    void add(OrderSetting orderSetting);
}
