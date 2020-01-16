package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class CheckGroupServiceImpl implements CheckGroupService {


    @Autowired
    CheckGroupDao checkGroupDao;

    /**
     * 步骤：
     *      1. 添加检查组(主键回显)
     *      2. 维护检查组和检查项的关系
     *
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    @Transactional
    public void add(Integer[] checkitemIds, CheckGroup checkGroup) {
        //1. 添加检查组(主键回显)
        checkGroupDao.add(checkGroup);
        //2. 维护检查组和检查项的关系
        if(checkGroup.getId() != null && checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.set(checkGroup.getId(), checkitemId);
            }
        }
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //1. 开始分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //2. 条件查询
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        //3. 创建返回值对象
        return new PageResult(page.getTotal(), page);
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public Integer[] findCheckItemIdsById(Integer id) {
        return checkGroupDao.findCheckItemIdsById(id);
    }

    /**
     * 步骤：
     *  1. 更新检查组信息（update）
     *  2. 维护检查组和检查项的关系
     *      a. 先删除原来的关系
     *      b. 添加新的关系
     *
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    @Transactional
    public void edit(Integer[] checkitemIds, CheckGroup checkGroup) {
        //1. 更新检查组信息（update）
        checkGroupDao.edit(checkGroup);
        //2. 维护检查组和检查项的关系
        //a. 先删除原来的关系
        checkGroupDao.delRelation(checkGroup.getId());
        // b. 添加新的关系
        if(checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.set(checkGroup.getId(), checkitemId);
            }
        }
    }

    /**
     * 1. 查询检查组是否被套餐表管理，如果关联，提示不能删除
     * 2. 如果没有关联， 删除检查组和检查项的关系
     * 3. 删除检查组
     * @param id
     */
    @Override
    public void delById(Integer id) {
        //1. 查询检查组是否被套餐表管理，如果关联，提示不能删除
        long count = checkGroupDao.findCountById(id);
        if(count > 0){
            throw new RuntimeException("检查组被套餐所关联，不能删除!!!");
        }
        checkGroupDao.delRelation(id);

        checkGroupDao.delById(id);

    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
