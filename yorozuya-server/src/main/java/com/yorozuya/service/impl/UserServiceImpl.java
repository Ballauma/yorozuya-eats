package com.yorozuya.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yorozuya.constant.JwtClaimsConstant;
import com.yorozuya.constant.MessageConstant;
import com.yorozuya.dto.UserLoginDTO;
import com.yorozuya.entity.User;
import com.yorozuya.exception.LoginFailedException;
import com.yorozuya.mapper.UserMapper;
import com.yorozuya.properties.JwtProperties;
import com.yorozuya.properties.WeChatProperties;
import com.yorozuya.service.UserService;
import com.yorozuya.utils.HttpClientUtil;
import com.yorozuya.utils.JwtUtil;
import com.yorozuya.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ballauma
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    WeChatProperties weChatProperties;

    public final static String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";


    @Override
    @Transactional
    public UserLoginVO wxLogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO);
// 判断 openid 是否为空，如果为空表示登录失败，抛出业务异常
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
// 判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openid);
        if (user == null) {
            // 新用户，自动完成注册
            user = User.builder()
                    .openid(openid)
                    .build();
            userMapper.insert(user);
        }
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());

        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return userLoginVO;
    }

    /**
     * 调用微信接口服务，获得当前微信用户的 openid
     *
     * @param userLoginDTO
     * @return
     */
    private String getOpenid(UserLoginDTO userLoginDTO) {
        // 调用微信接口服务，获得当前微信用户的 openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
