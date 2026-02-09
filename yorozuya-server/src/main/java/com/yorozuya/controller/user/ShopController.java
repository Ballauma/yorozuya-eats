package com.yorozuya.controller.user;

import com.yorozuya.result.Result;
import com.yorozuya.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ballauma
 */
@RestController("userShopController")
@Slf4j
@RequestMapping("/user/shop")
public class ShopController {
    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private ShopService shopService;

    /**
     * 获取店铺状态
     *
     * @return
     */
    @GetMapping("/status")
    public Result getStatus() {
        Integer status = shopService.getStatus();
        return Result.success(status);
    }
}
