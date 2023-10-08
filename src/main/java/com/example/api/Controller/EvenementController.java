package com.example.api.Controller;

import com.example.api.Models.ClubModel;
import com.example.api.Models.EtudiantModel;
import com.example.api.Models.EvenementModel;
import com.example.api.Models.EvenementStatut;
import com.example.api.Service.EvenementService;
import com.lowagie.text.DocumentException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

@RestController
@RequestMapping("evenement")
@Data
@CrossOrigin(origins = "*")
public class EvenementController {

    //Inject EvenementService to the controller to use its methods whith Autowired annotation
    @Autowired private EvenementService evenementService;

    //Envoi de la demande de création d'un événement
    @CrossOrigin
    @PostMapping(value = "/SendDemand")
    public ResponseEntity<EvenementModel> SendDemand(@RequestParam int idEtd, @RequestBody  EvenementModel evenementModel){
        try{
        EvenementModel evenement=evenementService.EnvoyerDem(idEtd, evenementModel);
        return new ResponseEntity<>(evenement, HttpStatus.OK);
    }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Validation de la création d'un événement
    @CrossOrigin
    @PutMapping(value = "/ValiderCreationEvent")
    public ResponseEntity<EvenementModel> ValiderCreationEvent(@RequestParam int idEvenement) {
        try {
            EvenementModel evenement = evenementService.ValiderCreationEvent(idEvenement);
            return new ResponseEntity<>(evenement, HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Récupération de la liste des événements par statut
    @CrossOrigin
    @GetMapping(value = "/GetEventByStatut")
    public ResponseEntity<List<EvenementModel>> GetEventByStatut(@RequestParam EvenementStatut statut) {
        try{
            List<EvenementModel> list= evenementService.getEventByStatus(statut);
            return new ResponseEntity<>(list, HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Récupération de la liste de tous les événements
    @CrossOrigin
    @GetMapping(value = "/GetAllEvent")
    public ResponseEntity<List<EvenementModel>> GetAllEvent() {
        try {
            List<EvenementModel> EventModelList = evenementService.getAllEvents();
            return new ResponseEntity<>(EventModelList, HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Confirmation de la participation d'un club à un événement

    @CrossOrigin
    @PostMapping(value = "/ConfirmParticipationClub_Event")
    public ResponseEntity<EvenementModel> ConfirmerParticipationClub_Event(@RequestParam int idClub,@RequestParam int idEvenement){
        try{
            EvenementModel evenement=evenementService.ConfirmerParticipationClub_Event(idClub,idEvenement);
            return new ResponseEntity<>(evenement, HttpStatus.OK);
        }
        catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Récuperer les participants d'un événement
    @CrossOrigin
    @GetMapping(value = "/GetParticipants_Club_Event")
    public ResponseEntity<List<EtudiantModel>>getEtudiantsParticipating(@RequestParam int idEvenement){
        try{
            List<EtudiantModel> list=evenementService.getEtudiantsParticipating(idEvenement);
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/get/libelle")
    public ResponseEntity<EvenementModel> getEventByLibelle(@RequestParam String eventLibelle){
        try {
            return new ResponseEntity<>(evenementService.getEventByLibelle(eventLibelle), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    /**
     * le nombre des événements par un club specifique (Dashboard)
     * @param clubName
     * @return
     */
    @CrossOrigin
    @GetMapping("/countEvents/club")
    public ResponseEntity<Integer> countEventsByClub(String clubName){
        try {
            return new ResponseEntity<>(evenementService.countEventsByClubName(clubName), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/generate/eventReport_pdf/")
    public void generatePdfFile(@RequestParam String eventLibelle, HttpServletResponse response) throws DocumentException, IOException
    {
        response.setContentType("application/pdf");
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=RapportEvenement : '" + eventLibelle + "'.pdf";
        response.setHeader(headerkey, headervalue);
        evenementService.genererListClubsParEvent(eventLibelle,response);
    }

    @GetMapping("get/clubs/event")
    public ResponseEntity<List<ClubModel>> getClubsByEvent(@RequestParam int idEvenement){
        try {
            return new ResponseEntity<>(evenementService.getClubsByEvent(idEvenement), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create/Event/Admin")
    public ResponseEntity<EvenementModel> CreateEventByAdmin(@RequestBody EvenementModel evenementModel){
        try {
            return new ResponseEntity<>(evenementService.CreerEventByAdmin(evenementModel), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
