package com.yorozuya.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Ballauma
 */
@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品 id 查询套餐 id
     *
     * @param dishIds 菜品 id 列表
     * @return 套餐 id 列表
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
