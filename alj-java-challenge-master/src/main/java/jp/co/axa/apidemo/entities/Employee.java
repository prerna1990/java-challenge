package jp.co.axa.apidemo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Employee implements Serializable {

	private static final long serialVersionUID = -4439114469417994311L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long employeeId;

	@ApiModelProperty(notes = "employee name")
	@Column(name = "EMPLOYEE_NAME")
	private String name;

	@ApiModelProperty(notes = "employee salary")
	@Column(name = "EMPLOYEE_SALARY")
	private Integer salary;

	@ApiModelProperty(notes = "employee department")
	@Column(name = "DEPARTMENT")
	private String department;

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof Employee)) {
			return false;
		}
		Employee employee = (Employee) o;
		return Objects.equals(this.employeeId, employee.employeeId) && Objects.equals(this.name, employee.name)
			   && Objects.equals(this.department, employee.department);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.employeeId, this.name, this.department);
	}

	@Override
	public String toString() {
		return "Employee{" + "id=" + this.employeeId + ", name='" + this.name + '\'' + ", role='" + this.department + '\'' + '}';
	}

}
