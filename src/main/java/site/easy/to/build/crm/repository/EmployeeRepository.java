package site.easy.to.build.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import site.easy.to.build.crm.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    // public List<Employee> saveAll(List<Employee> employees);
}
