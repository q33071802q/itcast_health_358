package com.itheiam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class DateDemo {

    public static void main(String[] args) {

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

    }
}
