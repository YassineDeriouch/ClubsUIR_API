package com.example.api.Repository;

import com.example.api.Models.DemandeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeRepository extends JpaRepository<DemandeModel,Integer> {
}
