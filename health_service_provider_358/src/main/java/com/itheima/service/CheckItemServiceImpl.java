package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class CheckItemServiceImpl implements  CheckItemService {

    @Autowired
    CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        //1. 开始分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //2. 条件查询
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        //创建返回值对象
        return new PageResult( page.getTotal() , page);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    /**
     * 步骤：
     *      1. 判断检查项是否被检查组关联，如果关联，提示用户不能删除
     *      2. 如果没有关联，删除检查项
     * @param id
     */
    @Override
    public void delById(Integer id) {
        //查询检查项的id在中间表出现了多少次
        long count = checkItemDao.findCountById(id);
        if(count > 0){
            throw new RuntimeException("检查项被检查组所关联，不能删除!!!");
        }

        checkItemDao.delById(id);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
