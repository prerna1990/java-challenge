package jp.co.axa.apidemo.controllers;

import java.util.List;
import javax.annotation.PostConstruct;
import jp.co.axa.apidemo.dtos.EmployeeDto;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.service.ApplicationStateService;
import jp.co.axa.apidemo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	private CacheManager cacheManager;

	@Autowired
    private EmployeeService employeeService;

	@Autowired
	private ApplicationStateService applicationStateService;

	@PreAuthorize("hasRole('VIEWER')")
	@GetMapping("/employees")
	public List<EmployeeDto> retrieveEmployeesList() {
		log.info("Generated message employee request received for all employees:");
		return employeeService.getAllEmployees();
	}

	@PreAuthorize("hasRole('VIEWER')")
	@GetMapping("/employees/{employeeId}")
	public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable(name = "employeeId") Long employeeId) {
		log.info("Generated message employee request received for: {}", employeeId);
		return employeeService.getEmployee(employeeId)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PreAuthorize("hasRole('EDITOR')")
	@PostMapping("/employees")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee createEmployee(EmployeeDto employee) { //List of employees.
		return employeeService.saveEmployee(employee);

	}

	@PreAuthorize("hasRole('EDITOR')")
	@DeleteMapping("/employees/{employeeId}")
	public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
		employeeService.deleteEmployee(employeeId);
		return new ResponseEntity<String>("Employee deleted successfully!.", HttpStatus.OK);
	}

	@PreAuthorize("hasRole('EDITOR')")
	@PutMapping("/employees/{employeeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Employee> saveOrUpdateEmployee(@RequestBody EmployeeDto employee,
		@PathVariable(name = "employeeId") Long employeeId) {
		log.info("Generated message employee before updation: {}", employee);
		return new ResponseEntity<>(employeeService.updateEmployee(employee, employeeId), HttpStatus.OK);

	}


	@PostConstruct
	public void postConstruct(){
		try {
			applicationStateService.prepareReadyState();
		} catch (InterruptedException e) {
			log.error("EXCEPTION: ", e);
		}
	}

	// clear all cache using cache manager
	@RequestMapping(value = "clearCache")
	public void clearCache(){
		for(String name:cacheManager.getCacheNames()){
			cacheManager.getCache(name).clear();
		}
	}

}
