package com.example.api.Service;
import com.example.api.Models.ClubModel;
import com.example.api.Models.EtudiantModel;
import com.example.api.Models.EventDocsModel;
import com.example.api.Models.RoleModel;
import com.example.api.Repository.ClubRepository;
import com.example.api.Repository.EtudiantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Yassine
 * @project Gestion-club-uir
 */

@Service
@Data
public class EtudiantService {

    @Autowired private ModelMapper modelMapper;

    private final EtudiantRepository etudiantRepository;
    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }
    @Transactional
    public EtudiantModel saveEtudiant(EtudiantModel etudiantModel){
        etudiantModel.setNom(etudiantModel.getNom());
        etudiantModel.setPrenom(etudiantModel.getPrenom());
        etudiantModel.setEmail(etudiantModel.getEmail());
        etudiantModel.setPassword(etudiantModel.getPassword());
        etudiantModel.setFkrole(etudiantModel.getFkrole());
        etudiantModel.setClubModelList(null);
        etudiantRepository.save(etudiantModel);
        return modelMapper.map(etudiantModel, EtudiantModel.class);
    }

    //Affecter etd To Club
    @Transactional
    public EtudiantModel AffectEtdToClub(int idEtd,int idClub) throws EntityNotFoundException{
        EtudiantModel etudiantModel= etudiantRepository.findById(idEtd).get();
        ClubModel clubModel= clubRepository.findById(idClub).get();
        clubModel.getEtudiantModelList().add(etudiantModel);
        return modelMapper.map(etudiantModel, EtudiantModel.class);
    }

    //Transactional is used to rollback the transaction in case of any error occurs during the transaction.
    @Transactional
    //getAllStudents() method is used to get all the students from the database.
    public List<EtudiantModel> getAllStudents() throws EntityNotFoundException{
        List<EtudiantModel> etudiantModelList = etudiantRepository.findAll();
        return etudiantModelList.stream().map(element -> modelMapper.map(element, EtudiantModel.class))
                .collect(Collectors.toList());
    }

    //Get student by id
    @Transactional
    public EtudiantModel getStudentById(int id) throws EntityNotFoundException{
        EtudiantModel etudiantModel = etudiantRepository.findById(id).get();
            return modelMapper.map(etudiantModel, EtudiantModel.class);
    }

    //Update student by id
    @Autowired
    private RoleService roleService;
    @Transactional
    public EtudiantModel updateStudentById(int id, EtudiantModel etudiantModel) throws EntityNotFoundException{
        EtudiantModel etudiantModel1 = etudiantRepository.findById(id).get();
        etudiantModel1.setNom(etudiantModel.getNom());
        etudiantModel1.setPrenom(etudiantModel.getPrenom());
        etudiantModel1.setEmail(etudiantModel.getEmail());
        etudiantModel1.setPassword(etudiantModel.getPassword());
        etudiantModel1.setTelephone(etudiantModel.getTelephone());
        // récupérer le rôle par son libellé
        RoleModel roleModel = roleService.getRoleByLibelle(etudiantModel.getFkrole().getLibelle());
        if(roleModel == null)
            System.err.println("roleModel = null");
        else
            System.out.println("roleModel = " + roleModel);

        etudiantModel1.setFkrole(roleModel);
       // etudiantModel1.setClubModelList(null);
        EtudiantModel etudiantModel2 = etudiantRepository.save(etudiantModel1);
        return modelMapper.map(etudiantModel2, EtudiantModel.class);
    }
    //delete STUDENT FROM CLUB --> club service
    @Autowired private ClubRepository clubRepository;
    @Transactional
    public List<EtudiantModel> getStudentsByClub(int id) {
        Optional<ClubModel> clubModelOptional = clubRepository.findById(id);
        if (clubModelOptional.isEmpty()) {
            throw new EntityNotFoundException("ClubController not found with id " + id);
        }
        ClubModel clubModel = clubModelOptional.get();
        List<EtudiantModel> etudiantModelList = clubModel.getEtudiantModelList();
        return etudiantModelList.stream().map(student -> modelMapper.map(student, EtudiantModel.class))
                .collect(Collectors.toList());
}

    /**
     * Get students by club name
     * @param libelle
     * @return
     */
    @Transactional
    public List<EtudiantModel> getStudentsByClubName(String libelle) {
        Optional<ClubModel> clubModelOptional = Optional.ofNullable(clubRepository.findClubModelByLibelle(libelle));
        if (clubModelOptional.isEmpty()) {
            throw new EntityNotFoundException("Club "+ libelle +"not found");
        }
        ClubModel clubModel = clubModelOptional.get();
        List<EtudiantModel> etudiantModelList = clubModel.getEtudiantModelList();
        return etudiantModelList.stream().map(student -> modelMapper.map(student, EtudiantModel.class))
                .collect(Collectors.toList());
}

}