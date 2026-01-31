package com.energybuddy.repository;

import com.energybuddy.model.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, Long> {
    
    List<EnergyConsumption> findByApplianceId(Long applianceId);
    
    @Query("SELECT ec FROM EnergyConsumption ec " +
           "JOIN ec.appliance a " +
           "WHERE a.user.id = :userId " +
           "ORDER BY ec.loggedDate DESC")
    List<EnergyConsumption> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT ec FROM EnergyConsumption ec " +
           "JOIN ec.appliance a " +
           "WHERE a.user.id = :userId " +
           "AND ec.loggedDate BETWEEN :startDate AND :endDate " +
           "ORDER BY ec.loggedDate DESC")
    List<EnergyConsumption> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT SUM(ec.energyKwh) FROM EnergyConsumption ec " +
           "JOIN ec.appliance a " +
           "WHERE a.user.id = :userId")
    Double getTotalEnergyKwhByUserId(@Param("userId") Long userId);
}
