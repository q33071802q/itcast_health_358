package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConst;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderSettingDao orderSettingDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderDao orderDao;


    /**
     * 步骤：
     *      1.判断是否进行了预约设置
     *          根据日期查询预约设置对象
     *              如果对象为null， 没有预约设置， 返回不可预约
     *              如果对象不为null, 继续
     *      2. 判断是否预约已满
     *          如果已预约大于等于可预约，预约已满，返回已满
     *          如果已预约小于可预约，继续
     *      3. 判断是否是会员
     *          根据手机号码查询会员对象
     *          如果会员对象为null， 不是会员，自动注册为会员， 继续预约
     *          如果会员对象不为null, 是会员，判断是否重复预约
     *              重复预约：同一个人预约了同一天同一个套餐
     *              以：会员id，日期，套餐id 为条件，查询预约信息，如果能查到，重复预约，返回不可重复预约
     *              如果没有查到，继续预约
     *      4. 开始预约
     *          添加预约信息到数据
     *      5. 已预约人数加 1
     *
     * @param map
     * @return
     */
    @Override
    public Result addOrder(Map<String, String> map) throws Exception {
        //获取手机号码
        String telephone = map.get("telephone");
        //获取日期
        String orderDateStr = map.get("orderDate");

        //套餐id
        Integer setmealId = Integer.parseInt(map.get("setmealId"));
        //转换字符串日期为日期对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = sdf.parse(orderDateStr);
        //1.判断是否进行了预约设置
        // 根据日期查询预约设置对象
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        // 如果对象为null， 没有预约设置， 返回不可预约
        if(orderSetting == null){
            return new Result(false, MessageConst.SELECTED_DATE_CANNOT_ORDER);
        }
        //2. 判断是否预约已满
        //如果已预约大于等于可预约，预约已满
        if(orderSetting.getReservations() >= orderSetting.getNumber()){
            return new Result(false,MessageConst.ORDER_FULL);
        }
        //3. 判断是否是会员
        //根据手机号码查询会员对象
        Member member = memberDao.findByTelephone(telephone);
        //如果会员对象为null， 不是会员，自动注册为会员， 继续预约
        if(member == null){
            //创建member对象
            member = new Member();
            //赋值
            String name = map.get("name");
            //姓名
            member.setName(name);
            //性别
            String sex = map.get("sex");
            member.setSex(sex);
            //身份证号码
            member.setIdCard(map.get("idCard"));
            //手机号码
            member.setPhoneNumber(map.get("telephone"));
            //注册时间
            member.setRegTime(new Date());
            //注册会员(主键回显)
            memberDao.add(member);
        }
        //如果会员对象不为null, 是会员，判断是否重复预约
        else{
            //重复预约：同一个人预约了同一天同一个套餐
            //以：会员id，日期，套餐id 为条件，查询预约信息，如果能查到，重复预约，返回不可重复预约
            //会员id
            Integer memberId = member.getId();
            //创建查询条件
            Order condition = new Order();
            condition.setMemberId(memberId);
            condition.setSetmealId(setmealId);
            condition.setOrderDate(orderDate);

            //查询预约对象
            long count = orderDao.findByOrder(condition);
            if(count > 0){
                return new Result(false, MessageConst.HAS_ORDERED);
            }
            //如果没有查到，继续预约
        }
        //4. 开始预约
        //    添加预约信息到数据
        //创建预约对象
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(orderDate);
        order.setOrderType(map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(setmealId);
        orderDao.addOrder(order);

        //5. 已预约人数加 1
        orderSetting.setReservations(orderSetting.getReservations() + 1);

        orderSettingDao.edit(orderSetting);
        return new Result(true, MessageConst.ORDER_SUCCESS, order);
    }

    @Override
    public Map<String, Object> findById(Integer id) {
        return orderDao.findById(id);
    }
}
