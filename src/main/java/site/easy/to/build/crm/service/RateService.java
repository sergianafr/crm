package site.easy.to.build.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Rate;
import site.easy.to.build.crm.repository.RateRepository;

@Service
public class RateService {
    @Autowired private RateRepository rateRepository;
    
    public Rate findMax() {
        return rateRepository.findMax().orElse(null);
    }
}
