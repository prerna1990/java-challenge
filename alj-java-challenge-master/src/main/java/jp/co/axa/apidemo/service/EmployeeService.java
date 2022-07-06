package jp.co.axa.apidemo.service;

import java.util.Optional;
import jp.co.axa.apidemo.dtos.EmployeeDto;
import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

public interface EmployeeService {

    public List<Employee> getAllEmployees();

    public Optional<Employee> getEmployee(Long employeeId);

    public Employee saveEmployee(Employee employee);

    public void deleteEmployee(Long employeeId);

    public Employee updateEmployee(Employee employee, Long employeeId);

}