package com.example.api.Controller;

import com.example.api.Models.*;
import com.example.api.Service.EvenementService;
import com.lowagie.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.text.DateFormat;
import java.util.List;

@RestController
@RequestMapping("evenement")
@Data
@CrossOrigin(origins = "*")
@Tag(name = "Evenement", description = "Gestion des Evenements")
public class EvenementController {

    //Inject EvenementService to the controller to use its methods whith Autowired annotation
    @Autowired private EvenementService evenementService;

    //Envoi de la demande de création d'un événement
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

    @PostMapping(value = "/saveEvent/club={selectedClubID}")
    public ResponseEntity<EvenementModel> saveEvent(@RequestBody  EvenementModel evenementModel, @PathVariable int selectedClubID){
        try{
        EvenementModel evenement=evenementService.saveEvent(evenementModel,selectedClubID);
        return new ResponseEntity<>(evenement, HttpStatus.OK);
    }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Validation de la création d'un événement
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

    @GetMapping("/get/events/By/Referent={idReferent}")
    public ResponseEntity<List<EvenementModel>> getEventsByReferent(@PathVariable int idReferent){
        try {
            return new ResponseEntity<>(evenementService.getEventsByReferent(idReferent),HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get/events/By/Etudiant")
    public ResponseEntity<List<EvenementModel>> getEventsByEtudiant(@RequestParam int idEtd){
        try {
            return new ResponseEntity<>(evenementService.getEventsByEtudiant(idEtd),HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/update/budget/event")
    public ResponseEntity<EvenementModel> UpdateBudgetEvent(@RequestParam int idevent,@RequestParam double Budget){
        try {
            return new ResponseEntity<>(evenementService.UpdateBudgetEvent(idevent,Budget),HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Upload event logo endpoint
     */

    @Operation(summary= "upload club logo",description = "Set/update event image in local system")
    @PostMapping(value = "/upload/image/idEvent={id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadEventImage(@RequestPart MultipartFile logo, @PathVariable int id, UriComponentsBuilder uriBuilder) throws FileSystemException {
        return new ResponseEntity<>(evenementService.saveEventImage(logo, id,uriBuilder), HttpStatus.OK);
    }
    @Operation(summary= "retrieve club logo",description = "Retrieve the club logo from the local system")
    @GetMapping("/get/image/idEvent={idEvent}")
    public ResponseEntity<Resource> getImage(@PathVariable int idEvent) throws IOException {
        return evenementService.getEventImage(idEvent);
    }

}
