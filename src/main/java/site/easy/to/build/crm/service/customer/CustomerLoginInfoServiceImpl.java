package site.easy.to.build.crm.service.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import site.easy.to.build.crm.repository.CustomerLoginInfoRepository;
import site.easy.to.build.crm.utility.ImportTemplate;
import site.easy.to.build.crm.entity.CustomerLoginInfo;

@Service
public class CustomerLoginInfoServiceImpl implements CustomerLoginInfoService {

    private final CustomerLoginInfoRepository customerLoginInfoRepository;

    public CustomerLoginInfoServiceImpl(CustomerLoginInfoRepository customerLoginInfoRepository) {
        this.customerLoginInfoRepository = customerLoginInfoRepository;
    }

    @Override
    public CustomerLoginInfo findById(int id) {
        return customerLoginInfoRepository.findById(id);
    }

    @Override
    public CustomerLoginInfo findByEmail(String email) {
        return customerLoginInfoRepository.findByUsername(email);
    }

    @Override
    public CustomerLoginInfo findByToken(String token) {
        return customerLoginInfoRepository.findByToken(token);
    }

    @Override
    public CustomerLoginInfo save(CustomerLoginInfo customerLoginInfo) {
        return customerLoginInfoRepository.save(customerLoginInfo);
    }

    @Override
    public void delete(CustomerLoginInfo customerLoginInfo) {
        customerLoginInfoRepository.delete(customerLoginInfo);
    }


    @Override
    public CustomerLoginInfo instanceFromCsv(String[] csvData){
        CustomerLoginInfo cu = new CustomerLoginInfo();
        cu.setEmail(csvData[0]);
        return cu;
    }
    @Override
    public List<CustomerLoginInfo> instanceAll(List<String[]> csvFile){
        List<CustomerLoginInfo> res = new ArrayList<>();
        for (int i = 1; i < csvFile.size(); i++) {
            res.add(instanceFromCsv(csvFile.get(i)));
        }
        return res;
    }

    @Override
    public List<CustomerLoginInfo> saveAll(List<CustomerLoginInfo> list){
        return customerLoginInfoRepository.saveAll(list);
    }
}
