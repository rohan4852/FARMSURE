package com.farmsure.repository;

import com.farmsure.model.WastageLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WastageLossRepository extends JpaRepository<WastageLoss, Long> {

    @Query("SELECT COALESCE(SUM(w.wastageQuantity), 0) FROM WastageLoss w")
    double sumWastageQuantity();

    @Query("SELECT COALESCE(SUM(w.lossQuantity), 0) FROM WastageLoss w")
    double sumLossQuantity();
}
