package com.yorozuya.annotation;

import com.yorozuya.enumeration.OperationType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于自动填充实体类的创建时间和更新时间
 * @author Ballauma
 */
@Target(java.lang.annotation.ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    /**
     * 操作类型：INSERT 或 UPDATE
     */
    OperationType value();
}
