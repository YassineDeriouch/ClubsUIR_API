package com.example.api.Service;

import com.example.api.Models.ReunionModel;
import com.example.api.Repository.ReunionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created By Youssef on 16/05/2023
 *
 * @Author : Youssef
 * @Date : 16/05/2023
 * @Project : Gestion-reunion-uir
 */

@Service
public class ReunionService {

    @Autowired private ReunionRepository reunionRepository;

    @Autowired private ModelMapper modelMapper;

    //Save Reunion
    @Transactional
    public ReunionModel SaveReunion(ReunionModel reunionModel){
        reunionModel.setId_reunion(reunionModel.getId_reunion());
        reunionModel.setDate_reunion(reunionModel.getDate_reunion());
        reunionModel.setLieu_reunion(reunionModel.getLieu_reunion());
        reunionModel.setListClubs(reunionModel.getListClubs());
        reunionModel.setListEtudiants(reunionModel.getListEtudiants());
        ReunionModel SavedReunion=reunionRepository.save(reunionModel);
        return modelMapper.map(SavedReunion,ReunionModel.class);
    }
    
    //Update Reunion
    @Transactional
    public ReunionModel UpdateReunion(ReunionModel reunionModel, int id) {
        Optional<ReunionModel> reunionModelOptional = reunionRepository.findById(id);
        if (reunionModelOptional.isPresent()) {
            ReunionModel reunionModel1 = modelMapper.map(reunionModel, ReunionModel.class);
            reunionModel1.setId_reunion(id);
            ReunionModel updated = reunionRepository.save(reunionModel1);
            return modelMapper.map(updated, ReunionModel.class);
        } else {
            System.out.println("this reunion does not exist !!");
            return null;
        }
    }

    //Delete Reunion By Id

    @Transactional
    public ReunionModel DeleteReunionById(int id) {
        Optional<ReunionModel> reunionModelOptional = reunionRepository.findById(id);
        if (reunionModelOptional.isPresent()) {
            reunionRepository.deleteById(id);
            return modelMapper.map(reunionModelOptional.get(), ReunionModel.class);
        } else {
            System.out.println("this reunion does not exist !!");
            return null;
        }
    }

    //Delete all reunions
    @Transactional
    public List<ReunionModel> DeleteAllReunions() {
        List<ReunionModel> reunionModelList = reunionRepository.findAll();
        for (ReunionModel reunionModel : reunionModelList) {
            reunionModel.getListClubs().size();// charge explicitement la relation
            reunionModel.getListEtudiants().size();// charge explicitement la relation
        }
        if (!reunionModelList.isEmpty()) {
            reunionRepository.deleteAll();
        } else {
            System.out.println("this reunion does not exist !");
            throw new EntityNotFoundException();
        }
        return reunionModelList;
    }


    //Get Reunion By Id
    @Transactional
    public ReunionModel GetReunionById(int id) {
        Optional<ReunionModel> reunionModelOptional = reunionRepository.findById(id);
        if (reunionModelOptional.isPresent()) {
            return modelMapper.map(reunionModelOptional.get(), ReunionModel.class);
        } else {
            System.out.println("this reunion does not exist !!");
            return null;
        }
    }

    //Get All Reunions
    @Transactional
    public List<ReunionModel> GetAllReunions() {
        List<ReunionModel> reunionModelList = reunionRepository.findAll();
        if (!reunionModelList.isEmpty()) {
            return reunionModelList.stream().map(reunionModel -> modelMapper.map(reunionModel, ReunionModel.class)).collect(Collectors.toList());
        } else {
            System.out.println("there is no reunions !!");
            return null;
        }
    }

}
