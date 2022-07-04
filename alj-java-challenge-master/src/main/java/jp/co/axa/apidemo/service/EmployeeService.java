package jp.co.axa.apidemo.service;

import java.util.Optional;
import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

public interface EmployeeService {

    public List<Employee> retrieveEmployees();

    public Optional<Employee> getEmployee(Long employeeId);

    public void saveEmployee(Employee employee);

    public void deleteEmployee(Long employeeId);

    public Employee updateEmployee(Employee employee, Long employeeId);

	//public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

	//public Page<Employee> findAll(Pageable pageable);
}