package jp.co.axa.apidemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import jp.co.axa.apidemo.common.EmployeeMapper;
import jp.co.axa.apidemo.common.ErrorCodes;
import jp.co.axa.apidemo.dtos.EmployeeDto;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.EmployeeNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	private static final String LOG_PREFIX = "Employee Request for update record: {} ";
	private final ObjectMapper objectMapper;

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private EmployeeMapper employeeMapper;

	@Override
	@Cacheable(value = "Employee")         // it will cache result and key name will be "Employee"
	public List<EmployeeDto> getAllEmployees() {
		log.info("----Getting employee data from database.----");
		return employeeMapper.toDto(employeeRepository.findAll());

	}

	@Override
	@Cacheable(value = "Employee", key = "#employeeId")
	public Optional<EmployeeDto> getEmployee(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException("Could not find the employee", ErrorCodes.EPGW0001 + "_" + employeeId.toString()));
		return Optional.ofNullable(employeeMapper.toDto(employee));
	}

	@Override
	@CacheEvict(value = "Employee", allEntries = true)  // It will clear cache when new Employee save to database
	public Employee saveEmployee(EmployeeDto employeeDTO) {
		return employeeRepository.save(employeeMapper.toEntity(employeeDTO));
	}

	@Override
	@CacheEvict(value = "Employee", key = "#employeeId")
	// @CacheEvict(value="Invoice", allEntries=true) //in case there are multiple entires to delete
	public void deleteEmployee(Long employeeId) {
		log.info("Generated message employee before deletion: {}", employeeId);
		Employee employee = employeeRepository.findById(employeeId)
			.orElseThrow(() -> new EmployeeNotFoundException("Could not find the employee", ErrorCodes.EPGW0002 + "_" + employeeId.toString()));
		employeeRepository.deleteById(employee.getId());
	}

	@Transactional
	@Modifying
	@Override
	@CachePut(value = "Employee", key = "#employeeId")
	public Employee updateEmployee(EmployeeDto employeeDto, Long employeeId) {

		try {
			log.info(LOG_PREFIX, objectMapper.writeValueAsString(employeeDto));
		} catch (JsonProcessingException e) {
			log.error(LOG_PREFIX, employeeDto);
		}

		employeeRepository.findById(employeeId)
			.map(employee1 -> employeeRepository.save(employeeMapper.toEntity(employeeDto)))
			.orElseGet(() -> {
				Employee employee = employeeMapper.toEntity(employeeDto);
				employee.setId(employeeId);
				employeeRepository.save(employee);
				return employee;
			});
		/*Approach
		 If employeeId exist
			then update the records.
		 else
			 create new record , here other approach could be throw the exception*/

		return null;
	}
}

