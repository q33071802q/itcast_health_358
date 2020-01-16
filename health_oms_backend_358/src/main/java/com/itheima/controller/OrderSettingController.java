package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {


    @Reference
    OrderSettingService orderSettingService;

    /**
     *
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            List<String[]> strsList = POIUtils.readExcel(excelFile);
            //把List<String[]> 转换为 List<OrderSetting>
            List<OrderSetting > orderSettingList = new ArrayList<>();

            for (String[] strs : strsList) {
                //一个string数组对一个一个OrderSetting对象
                OrderSetting orderSetting = new OrderSetting();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                orderSetting.setOrderDate(sdf.parse(strs[0]));
                orderSetting.setNumber(Integer.parseInt(strs[1]));
                //添加到集合中
                orderSettingList.add(orderSetting);
            }

            orderSettingService.addOrderSettings(orderSettingList);

            return new Result(true, MessageConst.IMPORT_ORDERSETTING_SUCCESS);

        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConst.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     *
     * @param month
     * @return
     */
    @RequestMapping("/findByMonth")
    public Result findByMonth(String month){
        try {
            List<OrderSetting> orderSettingList = orderSettingService.findByMonth(month);
            //数据类型转换
            List<Map<String,Object>> mapList = new ArrayList<>();
            for (OrderSetting orderSetting : orderSettingList) {
                //一个ordersetting对应一个map集合
                Map<String,Object> map = new HashMap<>();
                Date orderDate = orderSetting.getOrderDate();
                SimpleDateFormat sdf = new SimpleDateFormat("dd");
                map.put("date" ,  sdf.format(orderDate));
                map.put("number" , orderSetting.getNumber());
                map.put("reservations" ,orderSetting.getReservations() );
                //添加到集合中
                mapList.add(map);
            }
            return new Result(true,MessageConst.GET_ORDERSETTING_SUCCESS, mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody OrderSetting orderSetting){
        try {
            List<OrderSetting> orderSettingList = new ArrayList<>();
            orderSettingList.add(orderSetting);
            orderSettingService.addOrderSettings(orderSettingList);

            return new Result(true, MessageConst.ORDERSETTING_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConst.ORDERSETTING_FAIL);
        }
    }

}
