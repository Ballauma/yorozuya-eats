package com.yorozuya.mapper;

import com.github.pagehelper.Page;
import com.yorozuya.annotation.AutoFill;
import com.yorozuya.dto.DishPageQueryDTO;
import com.yorozuya.entity.Dish;
import com.yorozuya.enumeration.OperationType;
import com.yorozuya.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Ballauma
 */
@Mapper
public interface DishMapper {


    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
