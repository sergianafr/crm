package site.easy.to.build.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import site.easy.to.build.crm.entity.Employee;
import site.easy.to.build.crm.repository.EmployeeRepository;
import site.easy.to.build.crm.utility.Connect;
import site.easy.to.build.crm.utility.Import;

import java.util.ArrayList;
import java.util.List;
@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    
    public void importEmployee(MultipartFile file)throws Exception{
        Connect c = new Connect();
        try {
            List<String[]> data = Import.readCsvFile(file);
            c.connectToMySQL();
            int[] notNull = new int[1];
            String columns = "username, first_name, last_name, email, password, provider";
            String[] dataTypes = {"string", "string", "string", "string", "string", "string"};
            Import.insertToTemp(data, dataTypes, notNull,"employee", columns, c);
            c.getConnex().commit();
        } catch (Exception e) {
            throw e;
        }finally{
            c.closeBD();
        }
    }

    public List<String> importEmp(MultipartFile file)throws Exception{
        List<String> errors = new ArrayList<>();
        List<String[]> data = Import.readCsvFile(file);
        int[] notNull = new int[1];
        String columns = "username, first_name, last_name, email, password, provider";
        String[] dataTypes = {"string", "string", "string", "string", "string", "string"};
        errors = Import.getErrors(data, dataTypes, notNull);
        List<Employee> employees = new ArrayList<>();
        if(errors.isEmpty()){
            int rowCount = 0;
            for(int i = 1; i<data.size(); i++){
                String[] row = data.get(i);
                rowCount++;
                List<Object> values = Import.validateData(row, dataTypes, notNull, rowCount, errors);
                Employee emp = new Employee();
                emp.setUsername((String)values.get(0));
                emp.setFirstName((String)values.get(1));
                emp.setLastName((String)values.get(2));
                emp.setEmail((String)values.get(3));
                emp.setPassword((String)values.get(4));
                emp.setProvider((String)values.get(5));
                employees.add(emp);
            }
            employeeRepository.saveAll(employees);
            return errors;
        }
        return null;
    }
}
