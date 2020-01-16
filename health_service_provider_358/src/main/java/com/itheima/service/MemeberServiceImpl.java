package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class MemeberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /***
     *
     *  map:
             months:['2018-11','2018-12','2019-01']
             memberCount:  [10, 100,1000]
     * @return
     */
    @Override
    public Map<String, Object> getMemberReport() {
        //包含了最近一年的12个月份
        List<String> months = new ArrayList<>();
        //创建日历
        Calendar calendar = Calendar.getInstance();
        //返回12月以前
        calendar.add(Calendar.MONTH , -12);
        for (int i = 0; i < 12; i++) {
            //转换为字符串格式
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            String str = simpleDateFormat.format(calendar.getTime());
            months.add(str);
            //向前移一个月
            calendar.add(Calendar.MONTH , 1);
        }

        //统计每月月底的时候的会员总数量
        List<Long> memberCount  = new ArrayList<>();
        //month 没一个月
        for (String month : months) {
            month += "-31"; // month = month + "-31"
            long count = memberDao.findCountByMonth(month);
            memberCount.add(count);
        }

        //创建map集合
        Map<String,Object> map = new HashMap<>();
        map.put("months",months);
        map.put("memberCount",memberCount);

        return map;
    }
}
