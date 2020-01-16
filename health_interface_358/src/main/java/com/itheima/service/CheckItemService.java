package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface CheckItemService {
    void add(CheckItem checkItem);

    PageResult queryPage(QueryPageBean queryPageBean);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    void delById(Integer id);

    List<CheckItem> findAll();
}
