package com.yorozuya.service;

import com.yorozuya.dto.SetmealDTO;
import com.yorozuya.dto.SetmealPageQueryDTO;
import com.yorozuya.entity.Setmeal;
import com.yorozuya.result.PageResult;
import com.yorozuya.vo.DishItemVO;
import com.yorozuya.vo.SetmealVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ballauma
 */
@Service
public interface SetmealService {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 分页查询套餐
     *
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据 id 删除套餐，同时需要删除套餐和菜品的关联关系
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据 id 查询套餐和关联的菜品数据
     *
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);


    /**
     * 条件查询
     *
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据 id 查询菜品选项
     *
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

}
