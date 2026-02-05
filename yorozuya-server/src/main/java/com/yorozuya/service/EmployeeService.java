package com.yorozuya.service;

import com.yorozuya.dto.EmployeeDTO;
import com.yorozuya.dto.EmployeeLoginDTO;
import com.yorozuya.dto.EmployeePageQueryDTO;
import com.yorozuya.entity.Employee;
import com.yorozuya.result.PageResult;

/**
 * @author Ballauma
 */
public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工账号
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, long id);

    /**
     * 根据 id 查询员工
     *
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 更新员工信息
     *
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}
