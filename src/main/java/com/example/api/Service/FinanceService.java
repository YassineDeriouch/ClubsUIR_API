package com.example.api.Service;

import com.example.api.Models.EtudiantModel;
import com.example.api.Models.EvenementModel;
import com.example.api.Models.FinanceModel;
import com.example.api.Repository.FinanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Yassine
 * @project API
 */

@Service
@Data
public class FinanceService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FinanceRepository financeRepository;
    @Autowired
    private EvenementService evenementService ;

    @Autowired
    private EtudiantService etudiantService ;

    // AFFECTER BUDGET
    @Transactional
    public FinanceModel affecterBudget(int idEtd,String eventName, double budget, FinanceModel finance){
        finance.setId_financement(finance.getId_financement());
        finance.setLibelle(finance.getLibelle());
        finance.setBudget(finance.getBudget());
        EvenementModel evenementModel ;
        evenementModel = evenementService.getEventByLibelle(eventName);
        EtudiantModel etudiantModel=etudiantService.getStudentById(idEtd);
        if(evenementModel != null && etudiantModel != null){
            finance.setEventModel(evenementModel);
            finance.setBudget(budget);
            evenementModel.setBudget(budget);
            finance.setEtudiant(etudiantModel);
        }else{
            throw new EntityNotFoundException("the event : " + eventName + "does not exist !");
        }
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date year = new Date(sqlDate.getTime());
        finance.setAnnee(year);

        FinanceModel savedFinanceModel = financeRepository.save(finance);
        return  modelMapper.map(savedFinanceModel, FinanceModel.class);
    }

    //Update budget
    @Transactional
    public FinanceModel UpdateBudget(int id_financement,double budget){
        FinanceModel financeModel = financeRepository.findById(id_financement).orElseThrow(() -> new EntityNotFoundException("the finance : " + id_financement + "does not exist !"));
        financeModel.setBudget(budget);
        EvenementModel evenementModel=financeModel.getEventModel();
        evenementModel.setBudget(budget);
        financeModel.setEventModel(evenementModel);
        financeRepository.save(financeModel);
        return modelMapper.map(financeModel, FinanceModel.class);
    }


}
