package com.yorozuya.service;

import com.yorozuya.dto.UserLoginDTO;
import com.yorozuya.vo.UserLoginVO;
import org.springframework.stereotype.Service;

/**
 * @author Ballauma
 */
@Service
public interface UserService {
    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    UserLoginVO wxLogin(UserLoginDTO userLoginDTO);
}
