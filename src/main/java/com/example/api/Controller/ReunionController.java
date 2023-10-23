package com.example.api.Controller;

import com.example.api.Models.ClubModel;
import com.example.api.Models.EtudiantModel;
import com.example.api.Models.ReunionModel;
import com.example.api.Service.ReunionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created By Youssef on 16/05/2023
 *
 * @Author : Youssef
 * @Date : 16/05/2023
 * @Project : Gestion-club-uir
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reunion")
public class ReunionController {

    @Autowired private ReunionService reunionService;

    //Save Reunion Controller :

    @PostMapping("/save")
    public ResponseEntity<ReunionModel> SaveReunion(@RequestBody ReunionModel reunionModel){
        try{
            return new ResponseEntity<>(reunionService.SaveReunion(reunionModel), HttpStatus.OK);
        }catch (Exception e){
            e.getMessage();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update Reunion Controller :

    @PutMapping("/update")
    public ResponseEntity<ReunionModel> UpdateReunion(@RequestBody ReunionModel reunionModel,@RequestParam int id){
        try{
            return new ResponseEntity<>(reunionService.UpdateReunion(reunionModel,id), HttpStatus.OK);
        }catch (Exception e){
            e.getMessage();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete Reunion By id Controller :

    @DeleteMapping("/delete/id")
    public ResponseEntity<ReunionModel> DeleteReunionById(@RequestParam int id){
        try{
            return new ResponseEntity<>(reunionService.DeleteReunionById(id), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Delete All Reunion Controller :
    @DeleteMapping("/delete/all")
    public ResponseEntity<List<ReunionModel>> deleteAllReunions() {
        try{
            return new ResponseEntity<>(reunionService.DeleteAllReunions(), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Get Reunion By id Controller :
    @GetMapping("/get/id")
    public ResponseEntity<ReunionModel> GetReunionById(@RequestParam int id){
        try{
            return new ResponseEntity<>(reunionService.GetReunionById(id), HttpStatus.OK);
        }catch (Exception e){
            e.getMessage();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get All Reunions Controller :
    @GetMapping("/get/all")
    public ResponseEntity<List<ReunionModel>> GetAllReunions(){
        try{
            return new ResponseEntity<>(reunionService.GetAllReunions(), HttpStatus.OK);
        }catch (Exception e){
            e.getMessage();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/participants/reunion")
    public ResponseEntity<List<EtudiantModel>> GetParticipantsInMeeting(@RequestParam int idReunion) throws EntityNotFoundException{

            return new ResponseEntity<>(reunionService.GetParticipantsByReunion(idReunion), HttpStatus.OK);
    }

}
