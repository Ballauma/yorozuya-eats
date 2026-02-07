package com.yorozuya.mapper;

import com.github.pagehelper.Page;
import com.yorozuya.annotation.AutoFill;
import com.yorozuya.dto.SetmealPageQueryDTO;
import com.yorozuya.entity.Setmeal;
import com.yorozuya.enumeration.OperationType;
import com.yorozuya.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    void update(Setmeal setmeal);
}
