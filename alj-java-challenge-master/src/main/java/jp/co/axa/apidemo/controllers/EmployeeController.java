package jp.co.axa.apidemo.controllers;

import java.util.List;
import javax.annotation.PostConstruct;
import jp.co.axa.apidemo.common.ErrorCodes;
import jp.co.axa.apidemo.dtos.EmployeeDto;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.ServiceNotReadyException;
import jp.co.axa.apidemo.service.ApplicationStateService;
import jp.co.axa.apidemo.service.EmployeeService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
@Validated
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private ApplicationStateService applicationStateService;

	private final ModelMapper modelMapper = new ModelMapper();


	@PreAuthorize("hasRole('VIEWER')")
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> retrieveEmployeesList() {
		if (!applicationStateService.isReady()) {
			throw new ServiceNotReadyException("The service is not in a ready state", ErrorCodes.EPGW0003);
		}
		log.info("Generated message employee request received for all employees:");
		return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('VIEWER')")
	@GetMapping("/employees/{employeeId}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(name = "employeeId") Long employeeId) {
		log.info("Generated message employee request received for: {}", employeeId);
		return employeeService.getEmployee(employeeId)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PreAuthorize("hasRole('EDITOR')")
	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody EmployeeDto employeeDto) {
		return employeeService.saveEmployee(modelMapper.map(employeeDto, Employee.class));

	}

	@PreAuthorize("hasRole('EDITOR')")
	@DeleteMapping("/employees/{employeeId}")
	public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
		employeeService.deleteEmployee(employeeId);
		return new ResponseEntity<String>("Employee deleted successfully!.", HttpStatus.OK);
	}

	@PreAuthorize("hasRole('EDITOR')")
	@PutMapping("/employees/{employeeId}")
	public ResponseEntity<Employee> saveOrUpdateEmployee(@RequestBody EmployeeDto employeeDto,
		@PathVariable(name = "employeeId") Long employeeId) {
		log.info("Generated message employee before updation: {}", employeeDto);
		return new ResponseEntity<>(employeeService.updateEmployee(modelMapper.map(employeeDto, Employee.class), employeeId), HttpStatus.OK);

	}

	@PostConstruct
	public void postConstruct(){
		try {
			applicationStateService.prepareReadyState();
		} catch (InterruptedException e) {
			log.error("EXCEPTION: ", e);
		}
	}

}
