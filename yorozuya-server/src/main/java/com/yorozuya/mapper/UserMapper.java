package com.yorozuya.mapper;

import com.yorozuya.annotation.AutoFill;
import com.yorozuya.entity.User;
import com.yorozuya.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Ballauma
 */
@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);


    @AutoFill(value = OperationType.INSERT)
    void insert(User user);
}
