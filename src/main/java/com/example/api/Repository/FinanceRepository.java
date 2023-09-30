package com.example.api.Repository;

import com.example.api.Models.FinanceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yassine
 * @project API
 */
@Repository
public interface FinanceRepository extends JpaRepository<FinanceModel, Integer> {
}
