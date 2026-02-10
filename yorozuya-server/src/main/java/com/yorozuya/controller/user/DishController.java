package com.yorozuya.controller.user;

import com.yorozuya.constant.StatusConstant;
import com.yorozuya.entity.Dish;
import com.yorozuya.result.Result;
import com.yorozuya.service.DishService;
import com.yorozuya.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据分类 id 查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId) {
        // 构建 redis 中的 key，规则 dish_ 分类 id
        String key = "dish_" + categoryId;

        // 查询 redis 中是否存在数据
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);

        // 存在，直接返回
        if (list != null && list.size() > 0) {
            return Result.success(list);
        }
        // 不存在，查询数据库，将数据存入 redis

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);// 查询起售中的菜品

        list = dishService.listWithFlavor(dish);

        // 将数据存入 redis
        redisTemplate.opsForValue().set(key, list);
        return Result.success(list);
    }

}
