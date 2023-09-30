package com.example.api.Service;

import com.example.api.Models.ClubModel;
import com.example.api.Models.ReferentAcademiqueModel;
import com.example.api.Models.ReferentAcademiqueModel;
import com.example.api.Repository.ReferentAcademiqueRepository;
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
 * @author Yassine
 * @project API
 */
@Service
@Data
public class ReferentAcademiqueService {
    @Autowired
    private ReferentAcademiqueRepository referentRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * save referent (temporary for testing purposes only)
     *
     * @param referent
     * @return
     */
    @Transactional
    public ReferentAcademiqueModel saveReferent(ReferentAcademiqueModel referent) {
        referent.setNom(referent.getNom());
        referent.setPrenom(referent.getPrenom());
        referent.setEmail(referent.getEmail());
        referent.setPassword(referent.getPassword());
        referent.setClubModelList(referent.getClubModelList());
        ReferentAcademiqueModel saved = referentRepository.save(referent);
        return modelMapper.map(saved, ReferentAcademiqueModel.class);
    }

    /**
     * UPDATE REFERENT
     *
     * @param referent
     * @param id
     * @return
     */
    @Transactional
    public ReferentAcademiqueModel updateReferent(ReferentAcademiqueModel referent, int id) throws EntityNotFoundException{
        Optional<ReferentAcademiqueModel> referentOptional = referentRepository.findById(id);
        if (referentOptional.isPresent()) {
            ReferentAcademiqueModel referentAcademique = modelMapper.map(referent, ReferentAcademiqueModel.class);
            referentAcademique.setId_referent(id);
            ReferentAcademiqueModel updated = referentRepository.save(referentAcademique);
            return modelMapper.map(updated, ReferentAcademiqueModel.class);
        } else {
            throw new EntityNotFoundException("this club does not exist !!");
         }
    }

    /**
     * GET ALL
     * @return
     */
    @Transactional
    public List<ReferentAcademiqueModel> getAllReferents() throws EntityNotFoundException {
        return referentRepository.findAll().stream().map(element -> modelMapper.map(element, ReferentAcademiqueModel.class))
                .collect(Collectors.toList());
    }

    /**
     *  GET BY ID
     * @Param id
     */
    @Transactional
    public ReferentAcademiqueModel getReferentByID(int id) throws EntityNotFoundException{
        Optional<ReferentAcademiqueModel> referent = referentRepository.findById(id);
        return modelMapper.map(referent, ReferentAcademiqueModel.class);
    }


    /**
     * DELETE ROLE BY ID
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public ReferentAcademiqueModel deleteByID(int id) throws EntityNotFoundException{
        Optional<ReferentAcademiqueModel> roleOptional = referentRepository.findById(id);
        if(roleOptional.isPresent()){
            referentRepository.deleteById(id);
            return modelMapper.map(roleOptional,ReferentAcademiqueModel.class);
        }else{
            System.out.println();
            throw new EntityNotFoundException("this referent does not exist !");
        }
    }

    /**
     * DELETE ALL ROLES BY ID
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public ReferentAcademiqueModel deleteAll() throws EntityNotFoundException{
        List<ReferentAcademiqueModel> referentList = referentRepository.findAll();
        if (!referentList.isEmpty()) {
            referentRepository.deleteAll();
            return modelMapper.map(referentList, ReferentAcademiqueModel.class);
        } else {
            throw new EntityNotFoundException("The referent list is empty");
        }
    }
}


