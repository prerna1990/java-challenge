package jp.co.axa.apidemo.service;

import java.util.Optional;
import jp.co.axa.apidemo.dtos.EmployeeDto;
import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

public interface EmployeeService {

    public List<EmployeeDto> getAllEmployees();

    public Optional<EmployeeDto> getEmployee(Long employeeId);

    public Employee saveEmployee(EmployeeDto employeeDto);

    public void deleteEmployee(Long employeeId);

    public Employee updateEmployee(EmployeeDto employeeDto, Long employeeId);

}