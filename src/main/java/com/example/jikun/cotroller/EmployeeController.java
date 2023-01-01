package com.example.jikun.cotroller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.jikun.common.Result;
import com.example.jikun.entity.Employee;
import com.example.jikun.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    private static final String SALT = "$2a$10$NsoHQF5CN31rjouJB0nZ1a";

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = BCrypt.hashpw(employee.getPassword(),SALT);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        if(emp==null) return Result.error("账号不存在");
        if(!emp.getPassword().equals(password)) return Result.error("密码错误");
        if(emp.getStatus() == 0) return Result.error("账户已禁用");
        //登陆成功，将员工id存入Session中
        request.getSession().setAttribute("employee",emp.getId());
        return Result.success(emp);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        //清理Session中保存的员工id
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }
}
