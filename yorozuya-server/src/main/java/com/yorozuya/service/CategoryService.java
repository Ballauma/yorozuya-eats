package com.yorozuya.service;

import com.yorozuya.dto.CategoryDTO;
import com.yorozuya.dto.CategoryPageQueryDTO;
import com.yorozuya.entity.Category;
import com.yorozuya.result.PageResult;

import java.util.List;

/**
 * @author Ballauma
 */
public interface CategoryService {

    /**
     * 新增分类
     *
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据 id 删除分类
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 修改分类
     *
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 启用、禁用分类
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
