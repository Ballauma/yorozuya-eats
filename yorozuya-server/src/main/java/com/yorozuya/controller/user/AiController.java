package com.yorozuya.controller.user;


import com.yorozuya.result.Result;
import com.yorozuya.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 智能点餐推荐
 *
 * @author Ballauma
 */
@RestController
@RequestMapping("/user/ai")
@Slf4j
public class AiController {

    @Autowired
    private AiService aiService;

    @GetMapping("/chat")
    public Result<String> chat(@RequestParam String msg) {
        log.info("用户问AI: {}", msg);
        String reply = aiService.recommend(msg);
        return Result.success(reply);
    }
}
