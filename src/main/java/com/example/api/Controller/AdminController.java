package com.example.api.Controller;

import com.example.api.Models.AdminModel;
import com.example.api.Service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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

/**
 * @author Yassine Deriouch
 * @project API
 */

@Data
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping(value = "/save")
    public ResponseEntity<AdminModel> saveAdmin(@RequestBody AdminModel adminModel){
        try{
            return new ResponseEntity<>(adminService.saveAdmin(adminModel), HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/update/{idAdmin}")
    public ResponseEntity<AdminModel> updateAdmin(@PathVariable int idAdmin, @RequestBody AdminModel adminModel){
        try{
            return new ResponseEntity<>(adminService.updateAdmin(idAdmin,adminModel), HttpStatus.OK);
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


    /**
     * Upload and get admin profile picture endpoints
     */

    @PostMapping(value = "/upload/profilePicture/idAdmin={id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadAdminProfilePicture(@RequestPart MultipartFile logo, @PathVariable int id) throws FileSystemException {
        return new ResponseEntity<>(adminService.saveAdminProfilePicture(logo, id), HttpStatus.OK);
    }

    @GetMapping("/get/profilePicture/idAdmin={idAdmin}")
    public ResponseEntity<Resource> getImage(@PathVariable int idAdmin) throws IOException {
        return adminService.getAdminProfilePicture(idAdmin);
    }

}

