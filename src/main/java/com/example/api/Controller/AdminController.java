package com.example.api.Controller;

import com.example.api.Models.AdminModel;
import com.example.api.Service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yassine Deriouch
 * @project API
 */

@Data
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired private AdminService adminService;

    @CrossOrigin
    @PostMapping(value = "/save")
    public ResponseEntity<AdminModel> saveRole(@RequestBody AdminModel adminModel){
        try{
            return new ResponseEntity<>(adminService.saveAdmin(adminModel), HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin("*")
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<AdminModel> getRoleByID(@PathVariable int id){
        try{
            return new ResponseEntity<>(adminService.getAdminByID(id), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin
    @GetMapping(value = "/get/all")
    public ResponseEntity<List<AdminModel>> getAllRoles(){
        try {
            return new ResponseEntity<>(adminService.getAllAdmin(), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<AdminModel> deleteRoleByID(@PathVariable int id){
        try{
            return new ResponseEntity<>(adminService.deleteAdmin(id), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

