package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface CheckGroupDao {
    void add(CheckGroup checkGroup);

    void set(@Param("checkgroupId") Integer checkgroupId, @Param("checkitemId") Integer checkitemId);

    Page<CheckGroup> findByCondition(String queryString);

    CheckGroup findById(Integer id);

    Integer[] findCheckItemIdsById(Integer id);

    void edit(CheckGroup checkGroup);

    void delRelation(Integer id);

    long findCountById(Integer id);

    void delById(Integer id);

    List<CheckGroup> findAll();

    /**
     * 根据套餐id获取检查组集合
     * @param setmealId
     * @return
     */
    List<CheckGroup> findCheckGroupsBySetmealId(Integer setmealId);
}
