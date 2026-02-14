package com.yorozuya.mapper;

import com.yorozuya.annotation.AutoFill;
import com.yorozuya.entity.User;
import com.yorozuya.enumeration.OperationType;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Ballauma
 */
@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);


    @AutoFill(value = OperationType.INSERT)
    void insert(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    Integer countByMap(Map map);

    @MapKey("date")
    List<Map<String, Object>> countUserByDate(@Param("begin") LocalDate begin, @Param("end") LocalDate end);

    Integer countBeforeDate(LocalDateTime of);
}
