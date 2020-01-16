package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConst;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;

    /**
     * 步骤：
     *      1. 添加套餐（主键回显）
     *      2.  维护套餐和检查组的关系
     *
     * @param checkgroupIds
     * @param setmeal
     */
    @Override
    @Transactional
    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        // 1. 添加套餐（主键回显）
        setmealDao.add(setmeal);
        // 2.  维护套餐和检查组的关系
        if(setmeal.getId() != null && checkgroupIds != null && checkgroupIds.length > 0){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.set(setmeal.getId() , checkgroupId);
            }
        }
        //添加套餐完成：图片已经与数据库产生了联系
        jedisPool.getResource().sadd(RedisConst.SETMEAL_PIC_DB_RESOURCES , setmeal.getImg());

    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //开始分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //条件查询
        Page<Setmeal> page = setmealDao.findByCondition(queryPageBean.getQueryString());

        return new PageResult(page.getTotal(), page);
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    /**
     * 查询套餐详情（包括套餐对应的检查组信息，检查组对应的检查项信息）
     *  方案一：
     *      1. 根据套餐id查询套餐基本信息
     *      2. 根据套餐id查询对应的检查组信息
     *      3. 循环检查组，根据检查组id查询对应的检查项信息
     *      4.  把检查项添加到检查组中，把检查组添加到套餐中（数据组合）
     *
     *  方案二：
     *      直接访问dao，多表数据在mybatis中映射
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailsById(Integer id) {
        return setmealDao.findDetailsById(id);
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Map<String, Object>> getSetmealReport() {
        return setmealDao.getSetmealReport();
    }
}
