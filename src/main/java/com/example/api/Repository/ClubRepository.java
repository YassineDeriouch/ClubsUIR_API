package com.example.api.Repository;

import com.example.api.Models.ClubModel;
import com.example.api.Models.ClubStatut;
import com.example.api.Models.EtudiantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author Yassine
 * @project Gestion-club-uir
 */
@Repository
public interface ClubRepository extends JpaRepository<ClubModel, Integer> {
    ClubModel findClubModelByLibelle(String libelle);
    List<ClubModel> findClubModelByStatut(ClubStatut statut);

    @Query("SELECT c FROM ClubModel c WHERE c.statut = 'accepte' AND NOT EXISTS (SELECT 1 FROM c.etudiantModelList m WHERE m.id_etudiant = :idEtudiant)")
    List<ClubModel> findClubsByStatutWhereUserDoesNotExist(@Param("idEtudiant") int idEtudiant);}
