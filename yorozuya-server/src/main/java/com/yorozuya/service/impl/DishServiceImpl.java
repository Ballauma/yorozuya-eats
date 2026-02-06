package com.yorozuya.service.impl;

import com.yorozuya.dto.DishDTO;
import com.yorozuya.entity.Dish;
import com.yorozuya.entity.DishFlavor;
import com.yorozuya.mapper.DishFlavorMapper;
import com.yorozuya.mapper.DishMapper;
import com.yorozuya.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
