package site.easy.to.build.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import site.easy.to.build.crm.entity.CustomerLoginInfo;

public interface CustomerLoginInfoRepository extends JpaRepository<CustomerLoginInfo, Integer> {
    public CustomerLoginInfo findById(int id);

    public CustomerLoginInfo findByToken(String token);

    public CustomerLoginInfo findByUsername(String email);
    // public List<CustomerLoginInfo> saveAll(LiCst<CustomerLoginInfo> list);
}
