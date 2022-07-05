package jp.co.axa.apidemo.common;

import java.util.Optional;
import jp.co.axa.apidemo.dtos.EmployeeDto;
import jp.co.axa.apidemo.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper extends EntityMapper<Employee, EmployeeDto> {
	Optional<Object> toDto(Optional<Employee> byId);
}