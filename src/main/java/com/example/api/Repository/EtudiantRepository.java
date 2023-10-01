package com.example.api.Repository;

import com.example.api.Models.EtudiantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Youssef
 * @project Gestion-club-uir
 */
@Repository
public interface EtudiantRepository extends JpaRepository<EtudiantModel, Integer> {
    EtudiantModel findByEmail(String email);
    EtudiantModel findByPassword(String password);

}
