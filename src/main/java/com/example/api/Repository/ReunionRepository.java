package com.example.api.Repository;

import com.example.api.Models.ReunionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReunionRepository extends JpaRepository<ReunionModel, Integer>{

}
