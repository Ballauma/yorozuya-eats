package com.yorozuya.mapper;

import com.github.pagehelper.Page;
import com.yorozuya.annotation.AutoFill;
import com.yorozuya.dto.SetmealPageQueryDTO;
import com.yorozuya.entity.Setmeal;
import com.yorozuya.enumeration.OperationType;
import com.yorozuya.vo.DishItemVO;
import com.yorozuya.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author Ballauma
 */
@Mapper
public interface SetmealMapper {

    /**
     * 根据分类 id 查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);


    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);


    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long setmealId);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 动态条件查询套餐
     *
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐 id 查询菜品选项
     *
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);


    Integer countByMap(Map map);
}

