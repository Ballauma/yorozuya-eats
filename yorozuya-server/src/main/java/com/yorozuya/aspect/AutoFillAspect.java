package com.yorozuya.aspect;

import com.yorozuya.annotation.AutoFill;
import com.yorozuya.constant.AutoFillConstant;
import com.yorozuya.context.BaseContext;
import com.yorozuya.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动填充切面类
 *
 * @author Ballauma
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution(* com.yorozuya.mapper.*.*(..)) && @annotation(com.yorozuya.annotation.AutoFill)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        log.info("自动填充切面类，方法名：{}", joinPoint.getSignature().getName());
        // A. 获取当前拦截的方法上的注解值 (是 INSERT 还是 UPDATE?)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        // B. 获取目标方法的参数 (比如 Employee 实体对象)
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        Object entity = args[0];

        // C. 准备数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId(); // 从 ThreadLocal 获取当前登录用户 ID

        // D. 利用反射，给 entity 赋值
        if (operationType == OperationType.INSERT) {
            // 赋值 createTime, createUser, updateTime, updateUser
            try {
                entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class).invoke(entity, now);
                entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class).invoke(entity, currentId);
                entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(entity, now);
                entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, currentId);
            } catch (Exception e) {
                log.error("自动填充插入操作失败", e);
                return;
            }
        } else if (operationType == OperationType.UPDATE) {
            // 赋值 updateTime, updateUser
            try {
                entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(entity, now);
                entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, currentId);
            } catch (Exception e) {
                log.error("自动填充更新操作失败", e);
                return;
            }
        }
    }
}
