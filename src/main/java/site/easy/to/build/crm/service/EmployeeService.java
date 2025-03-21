package site.easy.to.build.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import site.easy.to.build.crm.utility.Connect;
import site.easy.to.build.crm.utility.Import;

import java.util.List;
@Service
public class EmployeeService {
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
}
