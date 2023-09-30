package com.example.api.Controller;

import com.example.api.Models.FinanceModel;
import com.example.api.Service.FinanceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yassine
 * @project API
 */

@RestController
@Data
@RequestMapping("Finance")
public class FinanceController {

    @Autowired
    public FinanceService financeService;

    @CrossOrigin
    @PostMapping("affecter/budget/event")
    public ResponseEntity<FinanceModel> affecterBudget(@RequestParam int idEtd,@RequestParam String eventName,@RequestParam double montant,
                                                       @RequestBody FinanceModel finance){
        try{
            return new ResponseEntity<>(financeService.affecterBudget(idEtd,eventName,montant, finance), HttpStatus.OK);
        }catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("update/budget/event")
    public ResponseEntity<FinanceModel>UpdateBudget(@RequestParam int id_financement,@RequestParam double budget){
        try {
            return new ResponseEntity<>(financeService.UpdateBudget(id_financement,budget),HttpStatus.OK);
        }
        catch (EntityNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
