package com.example.api.Controller;

import com.example.api.Models.ClubModel;
import com.example.api.Models.EtudiantModel;
import com.example.api.Models.ReunionModel;
import com.example.api.Service.ClubService;
import com.example.api.Service.EtudiantService;
import jakarta.persistence.Column;
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
 * @author Youssef
 * @project Gestion-club-uir
 */

@RestController
@Data
@CrossOrigin
@RequestMapping(value = "etudiant")
public class EtudiantController {
    @Autowired
    private EtudiantService etudiantService;
    // SAVE ETD ( temporary )
    @CrossOrigin                                      // FOR TESTING ONLY
    @PostMapping(value = "/save")
    public ResponseEntity<EtudiantModel> saveEtudiant(@RequestBody EtudiantModel etudiantModel) {
        return new ResponseEntity<>(etudiantService.saveEtudiant(etudiantModel), HttpStatus.OK);
    }
    // GET ALL
    @CrossOrigin
    @GetMapping(value = "/get/All")
    public ResponseEntity<List<EtudiantModel>> getAllStudents() {
        try {
        return new ResponseEntity<>(etudiantService.getAllStudents(), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // GET ETD BY ID
    @CrossOrigin
    @GetMapping(value = "/get/idEtudiant")
    public ResponseEntity<EtudiantModel> getStudentById(@RequestParam int id) {
        return new ResponseEntity<>(etudiantService.getStudentById(id), HttpStatus.OK);
    }

    // UPDATE ETD
    @CrossOrigin
    @PutMapping(value = "/update/idEtudiant")
    public ResponseEntity<EtudiantModel> updateStudentById(@RequestBody EtudiantModel etudiantModel, @RequestParam int id) {
        //on retourne une ResponseEntity avec le roleModel et un status ok
        try {
            return new ResponseEntity<>(etudiantService.updateStudentById(id, etudiantModel), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET ETDs BY CLUB
    @CrossOrigin
    @GetMapping(value = "/get/StudentByClub")
    public ResponseEntity<List<EtudiantModel>> getStudentByClub(@RequestParam int idClub) {
        try {
            List<EtudiantModel> list = etudiantService.getStudentsByClub(idClub);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            entityNotFoundException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get etd by club name
    @GetMapping(value = "/get/ByClub/name")
    public ResponseEntity<List<EtudiantModel>> getStudentByClubName(@RequestParam String clubName) {
        try {
            List<EtudiantModel> list = etudiantService.getStudentsByClubName(clubName);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Affect etd to club
    @PostMapping(value = "/affect/StudentToClub")
    public ResponseEntity<EtudiantModel> affectStudentToClub(@RequestParam int idEtudiant, @RequestParam int idClub) {
        try {
            EtudiantModel etudiantModel = etudiantService.AffectEtdToClub(idEtudiant, idClub);
            return new ResponseEntity<>(etudiantModel, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("list/students/of/EtdClub")
    public ResponseEntity<Map<String, List<EtudiantModel>>> getListofParticipantOfHisClub(@RequestParam int idStudent){
        try{
            return new ResponseEntity<>(etudiantService.ListParticipantOfHisClub(idStudent), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("list/reunion/of/etudiantClubs")
    public ResponseEntity<List<ReunionModel>> GetReunionByPresidentClubs(@RequestParam int idEtd){
        try{
            return new ResponseEntity<>(etudiantService.GetReunionByPresidentClubs(idEtd), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Upload and get etudiant profile picture endpoints
     */

    @PostMapping(value = "/upload/profilePicture/idEtudiant={id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadEtudiantProfilePicture(@RequestPart MultipartFile logo, @PathVariable int id) throws FileSystemException {
        return new ResponseEntity<>(etudiantService.saveEtudiantProfilePicture(logo, id), HttpStatus.OK);
    }

    @GetMapping("/get/profilePicture/idEtudiant={idEtudiant}")
    public ResponseEntity<Resource> getImage(@PathVariable int idEtudiant) throws IOException {
        return etudiantService.getEtudiantProfilePicture(idEtudiant);
    }

}
