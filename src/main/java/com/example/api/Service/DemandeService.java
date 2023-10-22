package com.example.api.Service;

import com.example.api.Models.ClubModel;
import com.example.api.Models.DemandeModel;
import com.example.api.Models.DemandeStatut;
import com.example.api.Models.EtudiantModel;
import com.example.api.Repository.ClubRepository;
import com.example.api.Repository.DemandeRepository;
import com.example.api.Repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DemandeService {

    @Autowired private EtudiantRepository etudiantRepository;

    @Autowired private ClubRepository clubRepository;

    @Autowired private DemandeRepository demandeRepository;
    public DemandeModel EnvoiDemandeIntegrationClub(int IdEtd,int Idclub) {
        Optional<EtudiantModel> opt_etd = etudiantRepository.findById(IdEtd);
        Optional<ClubModel> opt_club = clubRepository.findById(Idclub);
        if (opt_etd.isPresent() && opt_club.isPresent()) {
            EtudiantModel etudiant = opt_etd.get();
            ClubModel club = opt_club.get();
            DemandeModel demande = new DemandeModel();
            demande.setEtudiant(etudiant);
            demande.setClub(club);
            demande.setStatut(DemandeStatut.EN_ATTENTE);

            return demandeRepository.save(demande);
        }
        else {
           throw new RuntimeException("Etudiant ou club n'existe pas");
        }
    }

    public DemandeModel ValiderDemandeCreationClub(int idDem){
        Optional<DemandeModel> opt_dem = demandeRepository.findById(idDem);

        if (opt_dem.isPresent()){
            DemandeModel demande = opt_dem.get();
            demande.setStatut(DemandeStatut.APPROUVEE);
            EtudiantModel etudiant = demande.getEtudiant();
            ClubModel club = demande.getClub();
            club.getEtudiantModelList().add(etudiant);
            clubRepository.save(club);

            return demandeRepository.save(demande);
        }
        else {
            throw new IllegalStateException("Demande n'existe pas");
        }
    }

    public List<DemandeModel> listDemandeIntegrationClubPresident(int idEtd){
        Optional<EtudiantModel> opt_etd = etudiantRepository.findById(idEtd);
        List<ClubModel> clubs=new ArrayList<>();
        List<DemandeModel> demandes=new ArrayList<>();
        if (opt_etd.isPresent()){
            EtudiantModel president = opt_etd.get();
            clubs.addAll(president.getClubModelList());
            for (DemandeModel demande : demandeRepository.findAll()){
                if (clubs.contains(demande.getClub())){
                    demandes.add(demande);
                }
            }
            return demandes;
        }
        else {
            throw new IllegalStateException("Etudiant n'existe pas");
        }
    }

    public List<DemandeModel> DemandesPresidentClubEnAttente(int idEtd){
        List<DemandeModel> demandes=new ArrayList<>();
        for (DemandeModel demande : listDemandeIntegrationClubPresident(idEtd)){
            if (demande.getStatut().equals(DemandeStatut.EN_ATTENTE)){
                demandes.add(demande);
            }
        }
        return demandes;
    }

   public List<DemandeModel> DemandesPresidentClubApprouvee(int idEtd){
            List<DemandeModel> demandes=new ArrayList<>();
            for (DemandeModel demande : listDemandeIntegrationClubPresident(idEtd)){
                if (demande.getStatut().equals(DemandeStatut.APPROUVEE)){
                    demandes.add(demande);
                }
            }
            return demandes;
        }

}
