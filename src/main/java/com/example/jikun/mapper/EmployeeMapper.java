package com.example.jikun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.jikun.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
