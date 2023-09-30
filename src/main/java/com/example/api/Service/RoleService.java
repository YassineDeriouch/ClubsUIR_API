package com.example.api.Service;

import com.example.api.Models.RoleModel;
import com.example.api.Models.RoleModel;
import com.example.api.Repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class RoleService {
    @Autowired private ModelMapper modelMapper;
    @Autowired private RoleRepository roleRepository;

    /**
     * ADD ROLE
     * @param roleModel
     * @return
     */
    @Transactional
    public RoleModel addRoles(RoleModel roleModel)throws EntityNotFoundException{
        roleModel.setIdRole(roleModel.getIdRole());
        roleModel.setLibelle(roleModel.getLibelle());
        RoleModel saved = roleRepository.save(roleModel);
        return modelMapper.map(saved, RoleModel.class);
    }

    @Transactional
    public RoleModel updateRole(RoleModel roleModel, int idRole) throws EntityNotFoundException{
        Optional<RoleModel> roleOptional = roleRepository.findById(idRole);
        if(roleOptional.isPresent()){
            RoleModel roleModel1 = modelMapper.map(roleModel, RoleModel.class);
            roleModel1.setIdRole(idRole);
            RoleModel updated = roleRepository.save(roleModel1);
            return modelMapper.map(updated, RoleModel.class);
        }else{
            throw new EntityNotFoundException("not found");
        }
    }

    /**
     * GET ROLE BY NAME
     * @param libelle
     * @return
     */
    @Transactional
    public RoleModel getRoleByLibelle(String libelle) throws EntityNotFoundException{
        RoleModel roleModel = roleRepository.findRoleModelByLibelle(libelle);
        return modelMapper.map(roleModel, RoleModel.class);
    }

    /**
     * GET ROLE BY ID
     * @param id
     * @return
     */
    @Transactional
    public RoleModel getById(int id){
        return modelMapper.map(roleRepository.findById(id), RoleModel.class);
    }

    /**
     * GET ALL ROLES
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional(readOnly = true)             // GET ALL ROLES
    public List<RoleModel> findAll() throws EntityNotFoundException{
        return roleRepository.findAll().stream().map(element -> modelMapper.map(element, RoleModel.class))
                .collect(Collectors.toList());
    }

    /**
     * DELETE ROLE BY ID
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public RoleModel DeleteByID(int id) {
        Optional<RoleModel> roleOptional = roleRepository.findById(id);
        if(roleOptional.isPresent()){
            roleRepository.deleteById(id);
            return modelMapper.map(roleOptional,RoleModel.class);
        }else{
            System.out.println("this role does not exist !");
            throw new EntityNotFoundException();
        }
    }

    /**
     * DELETE ALL ROLES BY ID
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public RoleModel DeleteAll(){
        List<RoleModel> roleList = roleRepository.findAll();
        if (!roleList.isEmpty()) {
            roleRepository.deleteAll();
            return modelMapper.map(roleList, RoleModel.class);
        } else {
            System.out.println("The roles list is empty");
            throw new EntityNotFoundException();
        }
    }

}
