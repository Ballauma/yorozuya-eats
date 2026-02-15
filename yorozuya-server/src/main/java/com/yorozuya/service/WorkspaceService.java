package com.yorozuya.service;

import com.yorozuya.vo.BusinessDataVO;
import com.yorozuya.vo.DishOverViewVO;
import com.yorozuya.vo.OrderOverViewVO;
import com.yorozuya.vo.SetmealOverViewVO;
import java.time.LocalDateTime;

/**
 * @author Ballauma
 */
public interface WorkspaceService {

    /**
     * 根据时间段统计营业数据
     * @param begin
     * @param end
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 查询订单管理数据
     * @return
     */
    OrderOverViewVO getOrderOverView();

    /**
     * 查询菜品总览
     * @return
     */
    DishOverViewVO getDishOverView();

    /**
     * 查询套餐总览
     * @return
     */
    SetmealOverViewVO getSetmealOverView();

}
