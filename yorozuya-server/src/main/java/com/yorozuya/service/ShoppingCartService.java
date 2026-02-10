package com.yorozuya.service;

import com.yorozuya.dto.ShoppingCartDTO;
import com.yorozuya.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ballauma
 */
@Service
public interface ShoppingCartService {

    /**
     * 新增购物车商品
     *
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查询购物车商品
     *
     * @return
     */
    List<ShoppingCart> list();

}
