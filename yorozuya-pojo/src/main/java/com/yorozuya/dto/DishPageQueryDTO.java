package com.yorozuya.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Ballauma
 */
@Data
public class DishPageQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private String name;

    // 分类 id
    private Integer categoryId;

    // 状态 0 表示禁用 1 表示启用
    private Integer status;

}
