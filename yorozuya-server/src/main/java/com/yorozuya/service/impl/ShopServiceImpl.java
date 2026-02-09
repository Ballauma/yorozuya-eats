package com.yorozuya.service.impl;

import com.yorozuya.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Ballauma
 */
@Service
@Slf4j
public class ShopServiceImpl implements ShopService {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String KEY = "shop_status";

    @Override
    public void setStatus(Integer status) {
        log.info("准备修改店铺状态为: {}", status == 1 ? "营业中" : "打烊中");
        // 这里以后可以加逻辑：比如检查是否有未完成订单
        redisTemplate.opsForValue().set(KEY, status);
    }

    @Override
    public Integer getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        // 如果 Redis 里没数据，给个默认值（比如默认打烊）
        return status != null ? status : 0;
    }
}