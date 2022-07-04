package jp.co.axa.apidemo.service;

import java.util.Optional;
import javax.transaction.Transactional;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.EmployeeNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

	@Cacheable(value="employees")           // it will cache result and key name will be "employees"
	public List<Employee> retrieveEmployees() {
		log.info("----Getting employee data from database.----");
		return employeeRepository.findAll();

    }

    public Optional<Employee> getEmployee(Long employeeId) {
       return Optional.ofNullable(employeeRepository.findById(employeeId)
		   .orElseThrow(() -> new EmployeeNotFoundException(employeeId)));
       // return optEmp.get();
    }

	@Override
	@CacheEvict(value = "employees", allEntries=true)       // It will clear cache when new employee save to database
    public void saveEmployee(Employee employee){
        employeeRepository.save(employee);
    }

	@Override
	@CacheEvict(value = "employees",allEntries = true)      // It will clear cache when delete any employee to database
    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
    }

	@Transactional
	@Modifying
    public Employee updateEmployee(Employee employee, Long employeeId) {

		return this.getEmployee(employeeId)
			.map(x -> {
				return employeeRepository.save(employee);
			})
			.orElseGet(() -> {
				employee.setId(employeeId);
				return employeeRepository.save(employee);
			});
			// Approach.. exception to be thrown if no employee Id exists.
    }

	/*@Override
	public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.employeeRepository.findAll(pageable);
	}

	@Override
	public Page<Employee> findAll(Pageable pageable) {
		return null;
	}*/
}