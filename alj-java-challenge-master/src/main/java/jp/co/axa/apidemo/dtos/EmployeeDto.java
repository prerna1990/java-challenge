package jp.co.axa.apidemo.dtos;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("Employee")
public
class EmployeeDto implements Serializable {

	private String name;
	private Integer salary;
	private String department;
}
