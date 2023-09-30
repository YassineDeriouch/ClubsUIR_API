package com.example.api.Service;

import com.example.api.Models.*;
import com.example.api.Repository.ClubRepository;
import com.example.api.Repository.EtudiantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Yassine&Youssef
 * @project Gestion-club-uir
 */

@Data
@Service
public class ClubService {
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * SAVE CLUB
     *
     * @param clubModel
     * @return
     */
    @Transactional
    public ClubModel saveClub(ClubModel clubModel) {
        clubModel.setType(clubModel.getType());
        clubModel.setLibelle(clubModel.getLibelle());
        clubModel.setEtudiantModelList(clubModel.getEtudiantModelList());
        ClubModel club = clubRepository.save(clubModel);
        return modelMapper.map(club, ClubModel.class);
    }

    /**
     * UPDATE CLUB
     *
     * @param
     * @param id
     * @return
     */
    @Transactional
    public ClubModel updateClub(ClubModel clubModel, int id) {
        Optional<ClubModel> clubModelOptional = clubRepository.findById(id);
        if (clubModelOptional.isPresent()) {
            ClubModel existingClub = clubModelOptional.get();
            List<EtudiantModel> originalEtudiantList = existingClub.getEtudiantModelList(); // Store the original list
            List<DocumentModel> originalDocumentList = existingClub.getDocumentModelList();
            ReunionModel originalReunion = existingClub.getReunionModel();

            existingClub.setLibelle(clubModel.getLibelle());
            existingClub.setType(clubModel.getType());
            existingClub.setStatut(clubModel.getStatut());

            // Set the etudiantModelList back to its original value
            existingClub.setEtudiantModelList(originalEtudiantList);
            existingClub.setReunionModel(originalReunion);
            existingClub.setDocumentModelList(originalDocumentList);

            ClubModel updated = clubRepository.save(existingClub);
            return modelMapper.map(updated, ClubModel.class);
        } else {
            System.out.println("This club does not exist!");
            return null;
        }
    }




    /**
     * GET ALL CLUBS
     *
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional(readOnly = true)
    public List<ClubModel> findAll() throws EntityNotFoundException {
        return clubRepository.findAll().stream().map(element -> modelMapper.map(element, ClubModel.class))
                .collect(Collectors.toList());
    }

    /**
     * GET CLUB BY ID
     *
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public ClubModel findById(int id) throws EntityNotFoundException {
        try {
            return modelMapper.map(clubRepository.findById(id), ClubModel.class);
        } catch (EntityNotFoundException exception) {
            exception.getMessage();
            System.out.println("Club not found");
            return null;
        }
    }

    /**
     * GET CLUB BY LIBELLE
     *
     * @param Libelle
     * @return
     */
    @Transactional
    public ClubModel findByName(String Libelle) {
        try {
            return modelMapper.map(clubRepository.findClubModelByLibelle(Libelle), ClubModel.class);
        } catch (EntityNotFoundException exception) {
            exception.getMessage();
            System.out.println("Club  not found by the given name");
            return null;
        }
    }

    /**
     * DELETE CLUB BY ID
     *
     * @param id
     * @return
     */
    @Transactional
    public ClubModel deleteById(int id) {
        Optional<ClubModel> clubModelOptional = clubRepository.findById(id);
        if (clubModelOptional.isPresent()) {
            ClubModel clubModel = clubModelOptional.get();
            Hibernate.initialize(clubModel.getEtudiantModelList());
            Hibernate.initialize(clubModel.getDocumentModelList());
            clubRepository.deleteById(id);
            return modelMapper.map(clubModelOptional, ClubModel.class);
        } else {
            System.out.println("this club does not exist !");
            throw new EntityNotFoundException();
        }
    }

    /**
     * DELETE ALL CLUBS
     *
     * @return
     */
    @Transactional
    public ClubModel deleteAll() {
        List<ClubModel> clubModelList = clubRepository.findAll();
        if (!clubModelList.isEmpty()) {
            clubRepository.deleteAll();
            return modelMapper.map(clubModelList, ClubModel.class);
        } else {
            System.out.println("this club does not exist !");
            throw new EntityNotFoundException();
        }
    }

    /**
     * GET ALL PARTICIPANTS OF A SPECIFIC CLUB
     *
     * @param id
     * @return
     */
    @Transactional
    public List<EtudiantModel> getStudentsInClub(int id) { //get students in one club
        Optional<ClubModel> clubModelOptional = clubRepository.findById(id);
        if (!clubModelOptional.isPresent()) {
            throw new EntityNotFoundException("ClubController not found with id " + id);
        }
        ClubModel clubModel = clubModelOptional.get();
        List<EtudiantModel> etudiantModelList = clubModel.getEtudiantModelList().stream()
                .map(student -> modelMapper.map(student, EtudiantModel.class))
                .collect(Collectors.toList());
        return etudiantModelList;
    }

    //Injection EtudiantRepository
    @Autowired
    private EtudiantRepository etudiantRepository;

    //Envoyer une demande de création de club
    @Transactional
    public ClubModel EnvoyerDem(int idEtd, ClubModel clubModel) {
        EtudiantModel etudiantModel = etudiantRepository.findById(idEtd).get();
        clubModel.setType(clubModel.getType());
        clubModel.setLibelle(clubModel.getLibelle());
        if (clubModel.getEtudiantModelList() == null) {
            clubModel.setEtudiantModelList(new ArrayList<>()); // initialisation de la liste
        }
        clubModel.getEtudiantModelList().add(etudiantModel);
        clubModel.setReferent(clubModel.getReferent());
        //clubModel.setStatus(String.valueOf(ClubStatut.en_attente));
        clubModel.setStatut(ClubStatut.en_attente);
        ClubModel club = clubRepository.save(clubModel);
        return modelMapper.map(club, ClubModel.class);
    }

    //Get Club by status:
    @Transactional
    public List<ClubModel> getClubByStatus(ClubStatut clubStatut) throws EntityNotFoundException {
        return clubRepository.findClubModelByStatut(clubStatut).stream().map(element -> modelMapper.map(element, ClubModel.class))
                .collect(Collectors.toList());
    }

    //Valider demande de création de club
    @Transactional
    public ClubModel ValiderDemandeCreation(int idClub) {
        ClubModel clubModel = clubRepository.findById(idClub).orElseThrow(() -> new EntityNotFoundException("Club not found with id " + idClub));
        clubModel.setStatut(ClubStatut.accepte);
        ClubModel ClubValidated = clubRepository.save(clubModel);
        return modelMapper.map(ClubValidated, ClubModel.class);

    }

    // delete student from club
    @Transactional
    public ClubModel deleteStudentFromClub(String clubName, int idStudent) {
        Optional<ClubModel> clubModelOptional = Optional.ofNullable(clubRepository.findClubModelByLibelle(clubName));
        if (clubModelOptional.isEmpty()) {
            throw new EntityNotFoundException("CLub not found");
        }
        ClubModel clubModel = clubModelOptional.get();
        List<EtudiantModel> etudiantModelList = clubModel.getEtudiantModelList();
        EtudiantModel etudiantModelToRemove = etudiantModelList.stream()
                .filter(e -> e.getId_etudiant() == idStudent)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Student not found in club : "+clubName));

        etudiantModelList.remove(etudiantModelToRemove);
        clubModel.setEtudiantModelList(etudiantModelList);
        clubRepository.save(clubModel);
        return modelMapper.map(clubModel, ClubModel.class);
    }

    // NOMBRE TOTAL DES CLUBS (POUR DASHBOARD)
    @Transactional
    public int countAllClubs(){
        int totalClubs = (int) clubRepository.count();
        if(totalClubs == 0)
            throw new EntityNotFoundException(" 0 Clubs found !! ");
        else
            return totalClubs;
    }

    // NOMBRE TOTAL DES PARTICIPANTS D'UN CLUB SPECIFIQUE (POUR DASHBOARD)
    public int countClubParticipants(String clubName) {
        Optional<ClubModel> clubModel = Optional.ofNullable(clubRepository.findClubModelByLibelle(clubName));
        if (clubModel.isPresent()) {
            int count = clubModel.map(club -> club.getEtudiantModelList().size()).orElse(0);
            System.out.println("Count = "+ count);
            return count;
        }else {
            throw new EntityNotFoundException("club not found");
        }
    }



}

