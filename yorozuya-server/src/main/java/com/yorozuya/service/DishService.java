package com.yorozuya.service;

import com.yorozuya.dto.DishDTO;
import com.yorozuya.dto.DishPageQueryDTO;
import com.yorozuya.result.PageResult;
import org.springframework.stereotype.Service;

/**
 * @author Ballauma
 */
@Service
public interface DishService {

    /**
     * 新增菜品
     *
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
