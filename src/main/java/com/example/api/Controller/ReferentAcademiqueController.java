package com.example.api.Controller;

import com.example.api.Models.*;
import com.example.api.Service.ReferentAcademiqueService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.List;
import java.util.Map;

/**
 * @author Yassine
 * @project API
 */

@RestController
@Data
@RequestMapping (value = "referentAcademique")
@CrossOrigin(origins = "*")
public class ReferentAcademiqueController {

    @Autowired private ReferentAcademiqueService referentService;

    /**
     * SAVE REFERENT ( TEMPORARY, FOR TESTING ONLY)
     * @param referent
     * @return
     */

    @PostMapping (value = "/save")
    public ResponseEntity<ReferentAcademiqueModel> addReferent(@RequestBody ReferentAcademiqueModel referent){
       try {
           return new ResponseEntity<>(referentService.saveReferent(referent), HttpStatus.OK);
       }catch(Exception exception){
           exception.printStackTrace();
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    /**
     * UPDATE REFERENT
     * @param referent
     * @param id
     * @return
     */

    @PutMapping("/update/{id}")
    public ResponseEntity<ReferentAcademiqueModel> updateReferent(@RequestBody UserDTO referent, @PathVariable int id){
        try {
            return new ResponseEntity<>(referentService.updateReferent(referent, id), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET ALL REFERENT
     * @return
     */
    @GetMapping(value = "/get/all")
    public ResponseEntity<List<ReferentAcademiqueModel>> getAllReferents(){
        try{
            return new ResponseEntity<>(referentService.getAllReferents(), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET REFERENT BY ID
     * @param id
     * @return
     */
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<ReferentAcademiqueModel> getReferentByID(@PathVariable int id){
        try {
            return new ResponseEntity<>(referentService.getReferentByID(id), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity <ReferentAcademiqueModel> deleteReferentByID(@PathVariable int id){
        try {
            return new ResponseEntity<>(referentService.deleteByID(id), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/all")
    public ResponseEntity<ReferentAcademiqueModel> deleteAllRoles(){
        try{
            return new ResponseEntity<>(referentService.deleteAll(), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("list/students/club/referent")
    public ResponseEntity<List<EtudiantModel>> getReferentsByClub(@RequestParam int idRef){
        try{
            return new ResponseEntity<>(referentService.ListStudentsInHisClub(idRef), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("list/students/meeting/referent")
    public ResponseEntity<Map<String,List<EtudiantModel>>> GetParticipantInMeetingByClubReferent(int idReferent){
        try {
            return new ResponseEntity<>(referentService.GetParticipantsInMeetingsByClubReferent(idReferent), HttpStatus.OK);
        }
        catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/get/reunions/club/referent")
    public ResponseEntity<List<ReunionModel>> GetListReunionsOfClubRef(@RequestParam int idRef){
        try{
            return new ResponseEntity<>(referentService.ListReunionsOfHisClub(idRef), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Upload and get referent profile picture endpoints
     */

    @PostMapping(value = "/upload/profilePicture/idReferent={id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadReferentProfilePicture(@RequestPart MultipartFile logo, @PathVariable int id) throws FileSystemException {
        return new ResponseEntity<>(referentService.saveReferentProfilePicture(logo, id), HttpStatus.OK);
    }

    @GetMapping("/get/profilePicture/idReferent={idReferent}")
    public ResponseEntity<Resource> getImage(@PathVariable int idReferent) throws IOException {
        return referentService.getReferentProfilePicture(idReferent);
    }
}
