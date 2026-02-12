package com.yorozuya.mapper;

import com.yorozuya.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Ballauma
 */
@Mapper
public interface OrderDetailMapper {


    void insertBatch(List<OrderDetail> orderDetailList);
}
