package com.yorozuya.mapper;

import com.yorozuya.annotation.AutoFill;
import com.yorozuya.entity.DishFlavor;
import com.yorozuya.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Ballauma
 */
@Mapper
public interface DishFlavorMapper {

    @AutoFill(value = OperationType.INSERT)
    public void insertBatch(List<DishFlavor> flavors);

    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteBatchByDishIds(Long dishId);
}
