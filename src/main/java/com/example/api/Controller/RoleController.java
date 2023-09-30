package com.example.api.Controller;

import com.example.api.Models.ClubModel;
import com.example.api.Models.EtudiantModel;
import com.example.api.Models.RoleModel;
import com.example.api.Service.ClubService;
import com.example.api.Service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.List;

/**
 * @author Youssef
 * @project Gestion-club-uir
 */

@Data
@RestController
@RequestMapping(value = "role")
@CrossOrigin(origins = "*")
public class RoleController {
    @Autowired private RoleService roleService;


    @CrossOrigin
    @PostMapping(value = "/save")
    public ResponseEntity<RoleModel> saveRole(@RequestBody RoleModel roleModel){
        try{
            return new ResponseEntity<>(roleService.addRoles(roleModel), HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PutMapping(value = "/update/idRole")
    public ResponseEntity<RoleModel> updateRole(@RequestBody RoleModel roleModel, @RequestParam int id){
        try{
            return new ResponseEntity<>(roleService.updateRole(roleModel,id), HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/get/RoleName")
    //localhost:8080/role/get/RoleBylibelle?libelle=ROLE_ADMIN
    public ResponseEntity<RoleModel> getRoleBylibelle(@RequestParam String libelle){
        try {
            RoleModel roleModel = roleService.getRoleByLibelle(libelle);
            return new ResponseEntity<>(roleModel, HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.getMessage();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin("*")
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<RoleModel> getRoleByID(@PathVariable int id){
        try{
            return new ResponseEntity<>(roleService.getById(id), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin
    @GetMapping(value = "/get/all")
    public ResponseEntity<List<RoleModel>> getAllRoles(){
        try {
            return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<RoleModel> deleteRoleByID(@PathVariable int id){
        try{
            return new ResponseEntity<>(roleService.DeleteByID(id), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/delete/all")
    public ResponseEntity<RoleModel> deleteAllRoles(){
        try{
            return new ResponseEntity<>(roleService.DeleteAll(), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
        exception.printStackTrace();
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
