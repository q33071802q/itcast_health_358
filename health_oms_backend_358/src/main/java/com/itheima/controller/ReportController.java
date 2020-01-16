package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        try {
            Map<String,Object> map = memberService.getMemberReport();
            return new Result(true, MessageConst.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConst.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }


    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        /**
         * [
             {value:335, name:'直接访问'},
             {value:310, name:'邮件营销'},
             {value:234, name:'联盟广告'},
             {value:135, name:'视频广告'},
             {value:1548, name:'搜索引擎'}
         ]
         */try {
            List<Map<String,Object>> setmealCount = setmealService.getSetmealReport();

            //取出 setmealCount 中的所有的name值，存储到setmealNames 集合中
            List<Object> setmealNames = new ArrayList<>();
            for (Map<String, Object> map : setmealCount) {
                Object name=  map.get("name");
                setmealNames.add(name);
            }
            //创建返回值map集合
            Map<String,Object > map = new HashMap<>();
            map.put("setmealCount",setmealCount);
            map.put("setmealNames",setmealNames);

            return new Result(true,MessageConst.GET_SETMEAL_COUNT_REPORT_SUCCESS,map );
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.GET_SETMEAL_COUNT_REPORT_FAIL );
        }

    }


    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> map = reportService.getBusinessReportData();
            return new Result(true,MessageConst.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出运营数据到本地
     *      步骤
     *          1. 创建模板
     *          2. 获取运营统计数据
     *          3. 把运营数据写入到excel
     *          4. 下载excel到本地
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletResponse response){
        try {
            //2. 获取运营统计数据
            Map<String, Object> businessReportData = reportService.getBusinessReportData();
            //获取统计日期
            Object reportDate = businessReportData.get("reportDate");
            Object totalMember = businessReportData.get("totalMember");
            Object todayNewMember = businessReportData.get("todayNewMember");
            Object thisWeekNewMember = businessReportData.get("thisWeekNewMember");
            Object thisMonthNewMember = businessReportData.get("thisMonthNewMember");
            Object todayOrderNumber = businessReportData.get("todayOrderNumber");
            Object todayVisitsNumber = businessReportData.get("todayVisitsNumber");
            Object thisWeekOrderNumber = businessReportData.get("thisWeekOrderNumber");
            Object thisWeekVisitsNumber = businessReportData.get("thisWeekVisitsNumber");
            Object thisMonthOrderNumber = businessReportData.get("thisMonthOrderNumber");
            Object thisMonthVisitsNumber = businessReportData.get("thisMonthVisitsNumber");
            List<Map<String,Object>> hotSetmeal = (List<Map<String, Object>>) businessReportData.get("hotSetmeal");

            //3. 把运营数据写入到excel
            //获取excel的输入流对象
            InputStream inputStream = this.getClass().getResourceAsStream("/template/report_template.xlsx");
            //创建工作薄对象
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            //获取工作表对象（sheet）
            XSSFSheet sheet = workbook.getSheet("Sheet1");
            //设置统计日期
            //获取行对象
            Row row = sheet.getRow(2);
            //获取单元格
            Cell cell = row.getCell(5);
            //设置单元格的数据
            cell.setCellValue(String.valueOf(reportDate));

            //设置今日新增会员数
            //获取行对象
            row = sheet.getRow(4);
            //获取单元格
            cell = row.getCell(5);
            //设置单元格的数据
            cell.setCellValue(String.valueOf(todayNewMember));


            //设置总会员数
            //获取行对象
            row = sheet.getRow(4);
            //获取单元格
            cell = row.getCell(7);
            //设置单元格的数据
            cell.setCellValue(String.valueOf(totalMember));


            //设置本周新增会员数
            //获取行对象
            row = sheet.getRow(5);
            //获取单元格
            cell = row.getCell(5);
            //设置单元格的数据
            cell.setCellValue(String.valueOf(thisWeekNewMember));

            //设置本月新增会员数
            //获取行对象
            row = sheet.getRow(5);
            //获取单元格
            cell = row.getCell(7);
            //设置单元格的数据
            cell.setCellValue(String.valueOf(thisMonthNewMember));


            //设置今日预约数
            //获取行对象
            row = sheet.getRow(7);
            //获取单元格
            cell = row.getCell(5);
            //设置单元格的数据
            cell.setCellValue(String.valueOf(todayOrderNumber));

            //设置今日到诊数
            //获取行对象
            row = sheet.getRow(7);
            //获取单元格
            cell = row.getCell(7);
            //设置单元格的数据
            cell.setCellValue(String.valueOf(todayVisitsNumber));


            //设置本周预约数
            //获取行对象
            row = sheet.getRow(8);
            //获取单元格
            cell = row.getCell(5);
            //设置单元格的数据
            cell.setCellValue(String.valueOf(thisWeekOrderNumber));

            //设置本周到诊数
            //获取行对象
            row = sheet.getRow(8);
            //获取单元格
            cell = row.getCell(7);
            //设置单元格的数据
            cell.setCellValue(String.valueOf(thisWeekVisitsNumber));


            //设置本月预约数
            //获取行对象
            row = sheet.getRow(9);
            //获取单元格
            cell = row.getCell(5);
            //设置单元格的数据
            cell.setCellValue(String.valueOf(thisMonthOrderNumber));

            //设置本月到诊数
            //获取行对象
            row = sheet.getRow(9);
            //获取单元格
            cell = row.getCell(7);
            //设置单元格的数据
            cell.setCellValue(String.valueOf(thisMonthVisitsNumber));

            //声明行号
            int rownum = 12;
            for (Map<String, Object> map : hotSetmeal) {
                //获取行对象
                row = sheet.getRow(rownum);
                //
                cell = row.getCell(4);
                cell.setCellValue(String.valueOf(map.get("name")));
                //获取行，给行赋值
                cell = row.getCell(5);
                cell.setCellValue(String.valueOf(map.get("setmeal_count")));
                //获取行，给行赋值
                cell = row.getCell(6);
                cell.setCellValue(String.valueOf(map.get("proportion")));

                rownum ++ ;
            }

            //4. 下载excel到本地
            // 通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            //指定附件的格式为excel
            response.setContentType("application/vnd.ms-excel");
            //attachment 以附件下载
            //下载弹出框架中的默认的文件名
            response.setHeader("content-Disposition","attachment;filename="+reportDate+"_report.xlsx");
            //输出流下载
            workbook.write(out);
            out.flush();
            out.close();
            workbook.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
