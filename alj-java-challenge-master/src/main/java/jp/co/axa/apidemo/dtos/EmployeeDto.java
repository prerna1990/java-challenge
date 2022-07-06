package jp.co.axa.apidemo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@RedisHash("Employee")
public class EmployeeDto {

	private String name;
	private Integer salary;
	private String department;
}
