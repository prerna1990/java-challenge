package jp.co.axa.apidemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import jp.co.axa.apidemo.common.ErrorCodes;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.EmployeeNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@CacheConfig(cacheNames = "Employee")
public class EmployeeServiceImpl implements EmployeeService {

	private static final String LOG_PREFIX = "Employee Request for update record: {} ";
	public static final String NOT_FIND_THE_EMPLOYEE = "Could not find the employee";
	private final ObjectMapper objectMapper;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Value("10")
	private int pageSize;

	@Value("0")
	private int page;

	@Override
	@Cacheable(value = "Employee")         // it will cache result and key name will be "Employee"
	public List<Employee> getAllEmployees() {
		log.info("----Getting employee data from database.----");
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
		return employeeRepository.findAll(pageable).getContent();
	}

	@Override
	@Cacheable(value = "Employee", key = "#employeeId")
	public Optional<Employee> getEmployee(Long employeeId) {
		return Optional.ofNullable(employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(NOT_FIND_THE_EMPLOYEE, ErrorCodes.EPGW0001)));
	}

	@Override
	@CacheEvict(value = "Employee", allEntries = true)  // It will clear cache when new Employee save to database
	public Employee saveEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Transactional
	@Modifying
	@Override
	@CacheEvict(value = "Employee", key = "#employeeId")
	// @CacheEvict(value="Employee", allEntries=true) //in case there are multiple entires to delete
	public void deleteEmployee(Long employeeId) {
		log.info("Generated message employee before deletion: {}", employeeId);
		Employee employee = employeeRepository.findById(employeeId)
			.orElseThrow(() -> new EmployeeNotFoundException(NOT_FIND_THE_EMPLOYEE, ErrorCodes.EPGW0002));
		employeeRepository.deleteById(employee.getEmployeeId());
	}

	@Transactional
	@Modifying
	@Override
	@CachePut(value = "Employee", key = "#employeeId")
	@CacheEvict(value="Employee", allEntries=true)
	public Employee updateEmployee(Employee newEmployee, Long employeeId) {
		try {
			log.info(LOG_PREFIX, objectMapper.writeValueAsString(newEmployee));
		} catch (JsonProcessingException e) {
			log.error(LOG_PREFIX, newEmployee);
		}

		return employeeRepository.findById(employeeId)
			.map(employee -> Employee.builder().employeeId(employeeId).name(newEmployee.getName()).salary(newEmployee.getSalary()).department(newEmployee.getDepartment()).build())
			.map(employee -> employeeRepository.save(employee))
			.orElseThrow(() -> new EmployeeNotFoundException(NOT_FIND_THE_EMPLOYEE, ErrorCodes.EPGW0001)); // Approach 2
			/*.orElseGet(() -> { // Uncomment if want to follow approach 1
			newEmployee.setEmployeeId(employeeId);
			return employeeRepository.save(newEmployee);
		});*/
		/*Approach 1
		 If employeeId exist
			then update the records.
		 else
			 create new record
		Approach 2
		 If employeeId exist
			then update the records.
		 else
			 throw the exception
		 */

	}

}

