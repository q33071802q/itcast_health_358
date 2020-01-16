package com.itheiam;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class POIDemo {

    /**
     * 读取excel
     * @param args
     */
    public static void main1(String[] args) throws IOException {
        //创建工作薄对象（excel）
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\sun\\Desktop\\test.xlsx");
        //获取工作表对象
        XSSFSheet sheet = workbook.getSheetAt(0);
        //获取行对象
//        XSSFRow row = sheet.getRow(0);
//        System.out.println(row);
        //获取单元格
//        XSSFCell cell = row.getCell(1);
//        System.out.println(cell);

        int lastRowNum = sheet.getLastRowNum();
//        System.out.println("最后一行的行号：" + lastRowNum);

        for (int i = 0; i <= lastRowNum ; i++) {
            XSSFRow row = sheet.getRow(i);

            short lastCellNum = row.getLastCellNum();
//            System.out.println("该行有"+lastCellNum+"个单元格");
            for (int j = 0; j < lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);
                System.out.println(cell);
            }
        }

    }

    public static void main2(String[] args) throws IOException {
        //创建工作薄对象（excel）
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\sun\\Desktop\\test.xlsx");
        //根据名称获取sheet对象
        XSSFSheet sheet = workbook.getSheet("Sheet1");
        //
        for (Row row : sheet) {
            for (Cell cell : row) {
                //如果单元格数值类型
                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                    //可以当做字符串读取
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                }
                System.out.println(cell);
            }
        }

    }


    public static void main(String[] args) throws IOException {
        //创建工作薄对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表对象
        XSSFSheet sheet = workbook.createSheet("test");
        //创建行对象
        XSSFRow row = sheet.createRow(1);
        //创建单元格
        XSSFCell cell1 = row.createCell(5);
        //给cell赋值
        cell1.setCellValue("随便1");
        //创建单元格
        XSSFCell cell2 = row.createCell(6);
        //给cell赋值
        cell2.setCellValue("随便2");


        //创建行对象
        XSSFRow row1 = sheet.createRow(2);
        //创建单元格
        XSSFCell cell3 = row1.createCell(5);
        //给cell赋值
        cell3.setCellValue("都行");
        //创建单元格
        XSSFCell cell4 = row1.createCell(6);
        //给cell赋值
        cell4.setCellValue("怎么都行");

        //创建输出流对象
        OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\sun\\Desktop\\test.xlsx"));

        workbook.write(outputStream);


        outputStream.flush();
        outputStream.close();
        workbook.close();


    }
}

