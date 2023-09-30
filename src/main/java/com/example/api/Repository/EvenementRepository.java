package com.example.api.Repository;

import com.example.api.Models.ClubModel;
import com.example.api.Models.EvenementModel;
import com.example.api.Models.EvenementStatut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvenementRepository extends JpaRepository<EvenementModel, Integer> {

    List<EvenementModel> findEvenementModelByStatut(EvenementStatut statut);

    int countByParticipants(ClubModel club);

    EvenementModel findByLibelle(String eventName);
}
