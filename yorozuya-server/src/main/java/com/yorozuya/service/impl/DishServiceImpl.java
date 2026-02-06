package com.yorozuya.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yorozuya.constant.MessageConstant;
import com.yorozuya.dto.DishDTO;
import com.yorozuya.dto.DishPageQueryDTO;
import com.yorozuya.entity.Dish;
import com.yorozuya.entity.DishFlavor;
import com.yorozuya.exception.DeletionNotAllowedException;
import com.yorozuya.mapper.DishFlavorMapper;
import com.yorozuya.mapper.DishMapper;
import com.yorozuya.mapper.SetmealDishMapper;
import com.yorozuya.result.PageResult;
import com.yorozuya.service.DishService;
import com.yorozuya.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Ballauma
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;

    @Autowired
    DishFlavorMapper dishFlavorMapper;

    @Autowired
    SetmealDishMapper setmealDishMapper;


    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断菜品是否能删除
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == 1) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (!setmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除菜品
        for (Long id : ids) {
            dishMapper.deleteBatch(id);
            // 删除菜品口味
            dishFlavorMapper.deleteBatchByDishIds(id);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 新增菜品
        dishMapper.insert(dish);

        Long dishId = dish.getId();
        // 新增菜品口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (!flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            dishFlavorMapper.insertBatch(flavors);
        }

    }
}
