
package jp.co.axa.apidemo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.service.ApplicationStateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeMockMvcTests {

	@MockBean
	private EmployeeRepository employeeRepository;
	@MockBean
	private ApplicationStateService applicationStateService;
	@Autowired
	private MockMvc mockMvc;

	@Before
	public void init() {
		Employee employee = new Employee(1L, "Prerna", 123456, "PCM");
		when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
		final List<Employee> employeeList = List.of(employee);
		when(employeeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(employeeList));
		when(applicationStateService.isReady()).thenReturn(true);
	}

	@Test
	public void testGetEmployeeOK() throws Exception {
		mockMvc.perform(get("/api/v1/employees")
				.with(user("User").password("read_password").roles("VIEWER"))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	public void testGetEmployeeUnauthorized() throws Exception {

		mockMvc.perform(get("/api/v1/employees")
				.with(user("User").password("dummy").roles("ADMIN"))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError());
	}

	@Test
	public void testEmployeeUpdateOK() throws Exception {

		when(employeeRepository.save(any(Employee.class))).thenReturn(new Employee());
		String patchInJson = "{ \n"
							 + "   \"department\": \"MS\",\n"
							 + "   \"name\": \"Sonu\", \n"
							 + "   \"salary\": 12345\n"
							 + " }";
		mockMvc.perform(put("/api/v1/employees/1")
				.with(user("Admin").password("edit_password").roles("EDITOR"))
				.content(patchInJson)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		verify(employeeRepository, times(1)).findById(1L);
		verify(employeeRepository, times(1)).save(any(Employee.class));

	}

	@Test
	public void testDeleteInventory() throws Exception {

		doNothing().when(employeeRepository).deleteById(1L);
		mockMvc.perform(delete("/api/v1/employees/1")
				.with(user("Admin").password("edit_password").roles("EDITOR")))
			.andExpect(status().isOk());
		verify(employeeRepository, times(1)).deleteById(1L);
	}



}
