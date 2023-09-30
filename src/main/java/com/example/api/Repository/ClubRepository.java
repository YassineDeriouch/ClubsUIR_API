package com.example.api.Repository;

import com.example.api.Models.ClubModel;
import com.example.api.Models.ClubStatut;
import com.example.api.Models.EtudiantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yassine
 * @project Gestion-club-uir
 */
@Repository
public interface ClubRepository extends JpaRepository<ClubModel, Integer> {
    ClubModel findClubModelByLibelle(String libelle);
    List<ClubModel> findClubModelByStatut(ClubStatut statut);

}
