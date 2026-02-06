package com.yorozuya.mapper;

import com.github.pagehelper.Page;
import com.yorozuya.annotation.AutoFill;
import com.yorozuya.dto.EmployeePageQueryDTO;
import com.yorozuya.entity.Employee;
import com.yorozuya.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Ballauma
 */
@Mapper
public interface EmployeeMapper {


    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Insert("insert into employee (name,username,password,phone,sex,id_number,status,create_time,update_time,create_user,update_user)" +
            "values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);


    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);


    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);
}
