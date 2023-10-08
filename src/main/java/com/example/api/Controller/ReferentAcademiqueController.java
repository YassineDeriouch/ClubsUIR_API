package com.example.api.Controller;

import com.example.api.Models.EtudiantModel;
import com.example.api.Models.ReferentAcademiqueModel;
import com.example.api.Models.RoleModel;
import com.example.api.Service.ReferentAcademiqueService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ReferentAcademiqueModel> updateReferent(@RequestBody ReferentAcademiqueModel referent,@PathVariable int id){
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
}
