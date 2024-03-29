package com.example.api.Controller;

import com.example.api.Models.ClubModel;
import com.example.api.Models.ClubStatut;
import com.example.api.Models.EtudiantModel;
import com.example.api.Repository.ClubRepository;
import com.example.api.Service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.List;

/**
 * @author Yassine&Youssef
 * @project Gestion-club-uir
 */

@Data
@RequiredArgsConstructor
@CrossOrigin(origins="*")
@RestController
@RequestMapping(value = "club")
@Tag(name = "Club", description = "Clubs management API")
public class ClubController {
    private final ClubService clubService;

    /**
     * Save CLUB
     * @param clubModel
     * @return ResponseEntity
     */

    @Operation(summary= "save new club",description = "add a new club in the DB")
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
    @Operation(summary= "update club",description = "update the data of an existing club")
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

    @Operation(summary= "get one club",description = "Retrieve the details of one specific club ")
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
    @Operation(summary= "get one club by name",description = "Retrieve the details of one specific club by its name ")
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
    @Operation(summary= "get all clubs",description = "Retrieve all existing clubs in the DB ")
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
    @Operation(summary= "delete one club",description = "Delete one specific club from the system")
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
    @Operation(summary= "delete all clubs",description = "Delete all existing clubs at once")
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

    @Operation(summary= "get club memebers",description = "Retrieve all memebers of a specific")
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
    @Operation(summary= "request club creation",description = "send a club creation request to the admin")
    @PostMapping(value = "/send/demandeCreation")
    public ResponseEntity<ClubModel> sendDemande(@RequestParam int idEtd,@RequestBody ClubModel clubModel,UriComponentsBuilder uriBuilder){
        try{
            ClubModel cm = clubService.EnvoyerDem(idEtd,clubModel,uriBuilder);
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
    @Operation(summary= "get clubs by status",description = "Retrieve the list of clubs by a status")
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

    @Operation(summary= "get approved clubs where a student is not a memeber ",
               description = "Retrieve the list of clubs where a specific student is not a memeber")
    @GetMapping(value = "/get/accepte/etudiantNotExist={idEtudiant}")
    public ResponseEntity<List<ClubModel>> getClubAccepteWhereUserNotExist(@PathVariable int idEtudiant){
        try{
            List<ClubModel> list= clubService.getClubsAccepteWhereUserNotExists(idEtudiant);
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
    @Operation(summary= "approve the creation of a club",description = "Approve the pending request sent by a student to create a new club")
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

    @Operation(summary= "delete a student from a club",description = "Delete a specific member from a club")
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
    @Operation(summary= "Count the number of clubs",description = "Count the total number of all existing clubs")
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
    @Operation(summary= "Count the number of members of a clubs",
               description = "Count the total number of all existing members of a specific club")
    @GetMapping("/countParticipant/clubName")
    public ResponseEntity<Integer> countPART(@RequestParam String clubName){
        try {
            return new ResponseEntity<>(clubService.countClubParticipants(clubName), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary= "get clubs of a referent",description = "Retrieve the club list of a specific referent")
    @GetMapping(value = "/get/clubList/byReferent={idReferent}")
    public ResponseEntity<List<ClubModel>> getClubListByReferent(@PathVariable int idReferent){
        try{
            List<ClubModel> clubList= clubService.getClubListByReferent(idReferent);
            return new ResponseEntity<>(clubList, HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary= "get clubs of a student",description = "Retrieve the club list of a specific student")
    @GetMapping(value = "/get/clubList/byEtudiant={idEtudiant}")
    public ResponseEntity<List<ClubModel>> getClubListByEtudiant(@PathVariable int idEtudiant){
        try{
            List<ClubModel> clubList= clubService.getClubListByEtudiant(idEtudiant);
            return new ResponseEntity<>(clubList, HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Upload club logo endpoint
     */

    @Operation(summary= "upload club logo",description = "Set/update a new logo for a club to the local system")
    @PostMapping(value = "/upload/logo/idClub={id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadClubLogo(@RequestPart MultipartFile logo, @PathVariable int id,UriComponentsBuilder uriBuilder) throws FileSystemException {
        return new ResponseEntity<>(clubService.saveCLubLogo(logo, id,uriBuilder), HttpStatus.OK);
    }
    @Operation(summary= "retrieve club logo",description = "Retrieve the club logo from the local system")
    @GetMapping("/get/logo/idClub={idClub}")
    public ResponseEntity<Resource> getImage(@PathVariable int idClub) throws IOException {
        return clubService.getClubLogo(idClub);
    }


}
