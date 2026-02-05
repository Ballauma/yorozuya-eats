package com.yorozuya.mapper;

import com.github.pagehelper.Page;
import com.yorozuya.dto.CategoryPageQueryDTO;
import com.yorozuya.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Ballauma
 */
@Mapper
public interface CategoryMapper {

    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);


    void update(Category category);


    List<Category> list(Integer type);
}
