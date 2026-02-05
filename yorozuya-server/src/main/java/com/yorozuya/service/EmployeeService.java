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
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
}
