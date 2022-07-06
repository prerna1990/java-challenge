/*
package jp.co.axa.apidemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = DemoApplication.class)
@AutoConfigureMockMvc
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase
public class ApiDemoApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private EmployeeRepository repository;

	@After
	public void resetDb() {
		repository.deleteAll();
	}

	@Test
	public void whenValidInput_thenCreateEmployee() throws IOException, Exception {
		Employee bob = new Employee("bob");
		mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(bob)));

		List<Employee> found = repository.findAll();
		assertThat(found).extracting(Employee::getName).containsOnly("bob");
	}

	@Test
	public void givenEmployees_whenGetEmployees_thenStatus200() throws Exception {
		createTestEmployee("bob");
		createTestEmployee("alex");

		// @formatter:off
		mvc.perform(get("/api/employees").contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
			.andExpect(jsonPath("$[0].name", is("bob")))
			.andExpect(jsonPath("$[1].name", is("alex")));
		// @formatter:on
	}

	//

	private void createTestEmployee(String name) {
		Employee emp = new Employee(name);
		repository.saveAndFlush(emp);
	}

}
*/
