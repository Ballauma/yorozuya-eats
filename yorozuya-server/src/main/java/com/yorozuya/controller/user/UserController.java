package com.yorozuya.controller.user;

import com.yorozuya.dto.UserLoginDTO;
import com.yorozuya.result.Result;
import com.yorozuya.service.UserService;
import com.yorozuya.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ballauma
 */
@RestController
@Slf4j
@RequestMapping("/user/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录：{}", userLoginDTO);

        UserLoginVO UserLoginVO = userService.wxLogin(userLoginDTO);
        return Result.success(UserLoginVO);
    }
}
