package com.yorozuya.service;

import com.yorozuya.dto.EmployeeLoginDTO;
import com.yorozuya.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
