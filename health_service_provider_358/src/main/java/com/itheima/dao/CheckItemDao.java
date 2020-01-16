package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> findByCondition(String queryString);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    long findCountById(Integer id);

    void delById(Integer id);

    List<CheckItem> findAll();

    /**
     * 根据检查组id获取检查项信息
     * @param checkGroupId
     * @return
     */
    List<CheckItem> findCheckItemsByCheckGroupId(Integer checkGroupId);
}
