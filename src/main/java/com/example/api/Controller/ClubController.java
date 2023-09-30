package com.example.api.Controller;

import com.example.api.Models.ClubModel;
import com.example.api.Models.ClubStatut;
import com.example.api.Models.EtudiantModel;
import com.example.api.Service.ClubService;
import jakarta.persistence.Column;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yassine&Youssef
 * @project Gestion-club-uir
 */

@Data
@CrossOrigin(origins="*")
@RestController
@RequestMapping(value = "club")
public class ClubController {
    @Autowired private ClubService clubService;

    /**
     * Save CLUB
     * @param clubModel
     * @return ResponseEntity
     */
    @CrossOrigin
    @PostMapping(value = "/save")
    public ResponseEntity<ClubModel> saveClub (@Valid @RequestBody ClubModel clubModel){
        try {
            return new ResponseEntity<>(clubService.saveClub(clubModel), HttpStatus.OK);
        }catch (Exception exception){
            exception.getMessage();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *  update club
     * @param clubModel
     * @param id
     * @return
     */
    @CrossOrigin
    @PutMapping(value = "/update/idClub")
    public ResponseEntity <ClubModel> updateClub(@RequestBody ClubModel clubModel, @RequestParam int id){
        try{
            clubModel = clubService.updateClub(clubModel, id);
            return new ResponseEntity<>(clubModel, HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.getMessage();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET CLUB BY ID
     * @param id
     * @return ResponseEntity
     */

    @CrossOrigin
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<ClubModel> getClubByID(@PathVariable int id){
        try{
            ClubModel clubModel = clubService.findById(id);
            return new ResponseEntity<>(clubModel, HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.getMessage();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET CLUB BY NAME
     * @param libelle
     * @return ResponseEntity
     */
    @CrossOrigin
    @GetMapping(value = "/get/clubName")
    public ResponseEntity<ClubModel> getClubByName(@RequestParam String libelle){
        try {
            ClubModel clubModel = clubService.findByName(libelle);
            return new ResponseEntity<>(clubModel, HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.getMessage();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * List all clubs
     * @return ResponseEntity
     */
    @CrossOrigin
    @GetMapping(value = "/get/All")
    public ResponseEntity<List<ClubModel>> getAllClubs(){
        try {
            List<ClubModel> clubModelList = clubService.findAll();
            return new ResponseEntity<>(clubModelList, HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete club by ID
     * @return ResponseEntity
     */
    @CrossOrigin
    @DeleteMapping (value = "/delete/{id}")
    public ResponseEntity<ClubModel> deleteClubByID(@PathVariable int id){
        try{
            ClubModel clubModel = clubService.deleteById(id);
            System.out.println("clubModel : " + clubModel);
            return new ResponseEntity<>(clubModel, HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * DELETE ALL CLUBS
     * @return
     */
    @CrossOrigin
    @DeleteMapping (value = "/delete/All")
    public ResponseEntity<ClubModel> deleteAllClubs(){
        try{
            ClubModel clubModel = clubService.deleteAll();
            return new ResponseEntity<>(clubModel, HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET LES PARTICIPANTS DU CLUB
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/get/participants/idClub")
    public ResponseEntity<List<EtudiantModel>> getClubParticipants(@RequestParam int id){
        try{
            List<EtudiantModel> list= clubService.getStudentsInClub(id);
            return new ResponseEntity<>(list, HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * /DEMANDER CREATION CLUB
     * @param idEtd
     * @param clubModel
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/send/demandeCreation")
    public ResponseEntity<ClubModel> sendDemande(@RequestParam int idEtd,@RequestBody ClubModel clubModel){
        try{
            ClubModel cm = clubService.EnvoyerDem(idEtd,clubModel);
            return new ResponseEntity<>(cm, HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *  GET CLUB BY STATUS
     * @param status
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/get/ClubByStatus")
    public ResponseEntity<List<ClubModel>> getClubByStatus(@RequestParam ClubStatut status){
        try{
            List<ClubModel> list= clubService.getClubByStatus(status);
            return new ResponseEntity<>(list, HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * VALIDER CREATION DU CLUB
     * @param idClub
     * @return
     */
    @CrossOrigin
    @PutMapping(value = "/valider/CreationClub")
    public ResponseEntity<ClubModel> updateClubStatus(@RequestParam int idClub){
        try{
            ClubModel cm = clubService.ValiderDemandeCreation(idClub);
            return new ResponseEntity<>(cm, HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @CrossOrigin
    @DeleteMapping(value = "/delete/student={idEtudiant}/club={clubName}")
    public ResponseEntity<ClubModel> deleteStudentFromClub(@PathVariable String clubName, @PathVariable int idEtudiant) {
        try {
            ClubModel club = clubService.deleteStudentFromClub(clubName, idEtudiant);
            return new ResponseEntity<>(club, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET TOTAL OF EXISTING CLUBS
     */
    @CrossOrigin
    @GetMapping("/TotalClubs")
    public ResponseEntity<Integer> getTotalClubs(){
        try {
        return new ResponseEntity<>(clubService.countAllClubs(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Count PARTICIPANTS BY CLUB NAME
      * @param clubName
     * @return
     */

    @GetMapping("/countParticipant/clubName")
    public ResponseEntity<Integer> countPART(@RequestParam String clubName){
        try {
            return new ResponseEntity<>(clubService.countClubParticipants(clubName), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}