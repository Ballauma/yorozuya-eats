package com.yorozuya.service;

import com.yorozuya.dto.EmployeeDTO;
import com.yorozuya.dto.EmployeeLoginDTO;
import com.yorozuya.entity.Employee;

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
}
