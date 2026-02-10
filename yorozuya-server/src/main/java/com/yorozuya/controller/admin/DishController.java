package com.yorozuya.controller.admin;

import com.yorozuya.dto.DishDTO;
import com.yorozuya.dto.DishPageQueryDTO;
import com.yorozuya.entity.Dish;
import com.yorozuya.result.PageResult;
import com.yorozuya.result.Result;
import com.yorozuya.service.DishService;
import com.yorozuya.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author Ballauma
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品");
        // 清理菜品缓存
        cleanCache("dish_" + dishDTO.getCategoryId());

        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品");
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除菜品");

        // 清理菜品缓存
        cleanCache("dish_*");

        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据 id 查询菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品");
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 更新菜品
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    public Result<DishVO> update(@RequestBody DishDTO dishDTO) {
        log.info("更新菜品");
        // 清理菜品缓存
        cleanCache("dish_*");

        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 根据分类 id 查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 菜品起售停售
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        // 清理菜品缓存
        cleanCache("dish_*");

        dishService.startOrStop(status, id);
        return Result.success();
    }

    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
