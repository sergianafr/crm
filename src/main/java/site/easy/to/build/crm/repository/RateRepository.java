package site.easy.to.build.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.easy.to.build.crm.entity.Rate;

public interface RateRepository extends JpaRepository<Rate, Long> {
    @Query("SELECT r FROM Rate r WHERE r.createdAt = (SELECT MAX(r2.createdAt) FROM Rate r2)")
    Optional<Rate> findMax();

    public Rate save(Rate rate);
}
