package com.yorozuya.controller.user;

import com.yorozuya.dto.ShoppingCartDTO;
import com.yorozuya.entity.ShoppingCart;
import com.yorozuya.result.Result;
import com.yorozuya.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ballauma
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 新增购物车商品
     *
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("新增购物车商品{}", shoppingCartDTO);
        // 新增购物车商品
        shoppingCartService.addShoppingCart(shoppingCartDTO);

        return Result.success();
    }

    /**
     * 查询购物车商品
     *
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        // 查询购物车商品
        List<ShoppingCart> list = shoppingCartService.list();
        return Result.success(list);
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @DeleteMapping("/clean")
    public Result clean() {
        // 清空购物车
        shoppingCartService.clean();
        return Result.success();
    }

    /**
     * 减少购物车商品数量
     *
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        // 减少购物车商品数量
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}
