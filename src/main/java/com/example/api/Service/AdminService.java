package com.example.api.Service;

import com.example.api.Models.AdminModel;
import com.example.api.Repository.AdminRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Yassine Deriouch
 * @project API
 */
@Data
@Service
public class AdminService {

    @Autowired private ModelMapper modelMapper;
    @Autowired private AdminRepository adminRepository;

    /**
     * SAVE ADMIN
     * @param adminModel
     * @return
     */
    @Transactional
    public AdminModel saveAdmin(AdminModel adminModel){
        adminModel.setNom(adminModel.getNom());
        adminModel.setPrenom(adminModel.getPrenom());
        adminModel.setEmail(adminModel.getEmail());
        adminModel.setPassword(adminModel.getPassword());
        adminModel.setTelephone(adminModel.getTelephone());
        AdminModel saved = adminRepository.save(adminModel);
        return modelMapper.map(saved, AdminModel.class);
    }

    /**
     *  UPDATE ADMIN
     * @param idAdmin
     * @param adminModel
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public AdminModel updateAdmin(int idAdmin, AdminModel adminModel) throws EntityNotFoundException {
        Optional<AdminModel> adminOptional = adminRepository.findById(idAdmin);
        if(adminOptional.isPresent()){
           AdminModel adminModel1 =  modelMapper.map(adminOptional, AdminModel.class);
           adminModel1.setId_admin(idAdmin);
           AdminModel updated = adminRepository.save(adminModel1);
           return modelMapper.map(updated, AdminModel.class);
        }else{
            throw new EntityNotFoundException("Not found !");
        }
    }

    /**
     * GET ADMIN BY ID
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public AdminModel getAdminByID(int id) throws EntityNotFoundException{
        return modelMapper.map(adminRepository.findById(id), AdminModel.class);
    }

    /**
     * GET ALL ADMINS
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public List<AdminModel> getAllAdmin() throws EntityNotFoundException{
        return adminRepository.findAll().stream().map(element -> modelMapper.map(element, AdminModel.class))
                .collect(Collectors.toList());
    }

    /**
     * DELETE ADMIN BY ID
     * @param id
     * @return
     * @throws Exception
     */
    @Transactional
    public AdminModel deleteAdmin(int id) throws Exception {
        List<AdminModel> admins = adminRepository.findAll();
        if(admins.size() == 1){ // check if there is only one admin
            throw new Exception("Cannot delete admin. Must have at least one admin.");
        }
        Optional<AdminModel> adminOptional = adminRepository.findById(id);
        if(!adminOptional.isPresent()){
            throw new EntityNotFoundException("Admin with id " + id + " not found !");
        }
        adminRepository.deleteById(id);
        return modelMapper.map(adminOptional.get(), AdminModel.class);
    }


}
