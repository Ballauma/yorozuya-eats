package com.yorozuya.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yorozuya.utils.HttpClientUtil;
import com.yorozuya.config.DeepSeekProperties;
import com.yorozuya.entity.Dish;
import com.yorozuya.mapper.DishMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ballauma
 */
@Service
@Slf4j
public class AiService {

    @Autowired
    private DeepSeekProperties deepSeekProperties;

    @Autowired
    private DishMapper dishMapper; // 用来查菜品

    /**
     * 智能推荐菜品
     *
     * @param userMessage 用户说的话，例如 "我想吃辣的，预算50"
     */
    public String recommend(String userMessage) {
        // 1. 【RAG 核心】先从数据库把目前起售的菜品查出来
        Dish dishProbe = new Dish();
        dishProbe.setStatus(1); // 起售中
        List<Dish> dishList = dishMapper.list(dishProbe);

        // 简处理：只提取菜名和价格，拼成字符串喂给 AI
        StringBuilder menuContext = new StringBuilder("店铺现有菜单：");
        for (Dish dish : dishList) {
            menuContext.append(dish.getName()).append("(").append(dish.getPrice()).append("元), ");
        }

        // 2. 构造 Prompt (提示词)
        String systemPrompt = "你是一个外卖点餐助手。请根据用户的需求和以下菜单进行推荐。" +
                "请直接给出推荐组合和总价，语气要像万事屋的银时一样懒散但可靠。" +
                menuContext.toString();

        // 3. 调用 DeepSeek API
        return callDeepSeek(systemPrompt, userMessage);
    }

    private String callDeepSeek(String systemPrompt, String userMessage) {
        try {
            // 构造 HTTP 请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + deepSeekProperties.getApiKey());
            headers.put("Content-Type", "application/json");

            // 构造请求体 (OpenAI 格式)
            JSONObject body = new JSONObject();
            body.put("model", deepSeekProperties.getModel());
            body.put("temperature", 0.7); // 创意程度

            JSONArray messages = new JSONArray();
            // 系统人设
            JSONObject sysMsg = new JSONObject();
            sysMsg.put("role", "system");
            sysMsg.put("content", systemPrompt);
            messages.add(sysMsg);
            // 用户消息
            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            messages.add(userMsg);

            body.put("messages", messages);

            // 发送 POST 请求
            log.info("发送请求给DeepSeek: {}", body.toJSONString());
            String response = HttpClientUtil.doPostJson(deepSeekProperties.getBaseUrl(), headers, body.toJSONString());
            log.info("DeepSeek 返回: {}", response);
            // 解析响应
            JSONObject resJson = JSON.parseObject(response);
            // DeepSeek 返回结构: choices[0].message.content
            return resJson.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

        } catch (Exception e) {
            log.error("AI 调用失败", e);
            return "啊... DeepSeek 好像去买草莓牛奶了，暂时联系不上。(调用失败)";
        }
    }
}