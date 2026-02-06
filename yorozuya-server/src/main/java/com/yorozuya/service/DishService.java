package com.yorozuya.service;

import com.yorozuya.dto.DishDTO;
import org.springframework.stereotype.Service;

/**
 * @author Ballauma
 */
@Service
public interface DishService {

    /**
     * 新增菜品
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

}
