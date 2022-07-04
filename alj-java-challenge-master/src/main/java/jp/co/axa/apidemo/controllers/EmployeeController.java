package jp.co.axa.apidemo.controllers;

import java.util.Optional;
import javax.annotation.PostConstruct;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.service.ApplicationStateService;
import jp.co.axa.apidemo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


	// display list of employees
	/*@GetMapping("/")
	public Page<Employee> viewHomePage(Model model) {
		return findPaginated(1, "name", "asc", model);
	}
*/
	//@PreAuthorize("hasRole('VIEWER')")
	@GetMapping("/employees")
    public List<Employee> getEmployees() {
		return employeeService.retrieveEmployees();
    } // pagination

	@PreAuthorize("hasRole('VIEWER')")
    @GetMapping("/employees/{employeeId}")
    public Optional<Employee> getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        return employeeService.getEmployee(employeeId);
    }

	@PreAuthorize("hasRole('EDITOR')")
    @PostMapping("/employees")
	@ResponseStatus(HttpStatus.CREATED)
    public void saveEmployee(Employee employee){ //List of employees.
        employeeService.saveEmployee(employee);
      //  System.out.println("Employee Saved Successfully");
    }

	@PreAuthorize("hasRole('EDITOR')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/employees/{employeeId}")
    public void deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        employeeService.deleteEmployee(employeeId);
      //  System.out.println("Employee Deleted Successfully");
    }

	@PreAuthorize("hasRole('EDITOR')")
    @PutMapping("/employees/{employeeId}")
	@CachePut(cacheNames="employee", key="#id")
    public void saveOrUpdateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){

            employeeService.updateEmployee(employee,employeeId);
        }



	@GetMapping("/page/{pageNo}")
	public Page<Employee> findPaginated(@PathVariable (value = "pageNo") int pageNo,
		@RequestParam("sortField") String sortField,
		@RequestParam("sortDir") String sortDir,
		Model model) {
		int pageSize = 5;

		//Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
		//return  page;

	//	List<Employee> listEmployees = page.getContent();

		/*model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listEmployees", listEmployees);*/
		//return listEmployees;
		return null;
	}

	/*@RequestMapping(value = "/listPageable", method = RequestMethod.GET)
	Page<Employee> employeesPageable(Pageable pageable) {
		return employeeService.findAll(pageable);

	}*/

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
