package com.example.api.Service;

import com.example.api.Models.ClubModel;
import com.example.api.Models.EtudiantModel;
import com.example.api.Models.EvenementModel;
import com.example.api.Models.EvenementStatut;
import com.example.api.Repository.ClubRepository;
import com.example.api.Repository.EvenementRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Youssef
 * EvenementService
 */
@Service
@Data
public class EvenementService {

    @Autowired private ModelMapper modelMapper;

    private final EvenementRepository evenementRepository;
    public EvenementService(EvenementRepository evenementRepository) {
        this.evenementRepository = evenementRepository;
    }

    //Send demand to create an event
    @Autowired private EtudiantService etudiantService;
    public EvenementModel EnvoyerDem(int idEtd, EvenementModel evenementModel){
        EtudiantModel etudiantModel= etudiantService.getStudentById(idEtd);
        evenementModel.setId_evenement(evenementModel.getId_evenement());
        evenementModel.setLibelle(evenementModel.getLibelle());
        evenementModel.setDate_debut(evenementModel.getDate_debut());
        evenementModel.setDate_fin(evenementModel.getDate_fin());
        evenementModel.setType(evenementModel.getType());
        evenementModel.setBudget(evenementModel.getBudget());
        evenementModel.setParticipants(evenementModel.getParticipants());
        evenementModel.setStatut(EvenementStatut.en_attente);
        EvenementModel evenement = evenementRepository.save(evenementModel);
        return modelMapper.map(evenement, EvenementModel.class);
    }

    //Validate the creation of an event
    public EvenementModel ValiderCreationEvent(int idEvenement){

        EvenementModel evenementModel=evenementRepository.findById(idEvenement).get();

        evenementModel.setStatut(EvenementStatut.accepte);

        EvenementModel evenement = evenementRepository.save(evenementModel);

        return modelMapper.map(evenement,EvenementModel.class);
    }
    public List<EvenementModel> getEventByStatus(EvenementStatut statut){
        return evenementRepository.findEvenementModelByStatut(statut).stream().map(element -> modelMapper.map(element, EvenementModel.class))
                .collect(Collectors.toList());

    }
    public List<EvenementModel> getAllEvents(){
        return evenementRepository.findAll().stream().map(element -> modelMapper.map(element, EvenementModel.class))
                .collect(Collectors.toList());
    }

    @Autowired private ClubService clubService;
    public EvenementModel ConfirmerParticipationClub_Event(int idClub,int idEvenement){

        ClubModel clubModel=clubService.findById(idClub);

        EvenementModel evenementModel=evenementRepository.findById(idEvenement).get();

        List<ClubModel> Participants=evenementModel.getParticipants();

        Participants.add(clubModel);

        evenementRepository.save(evenementModel);

        return evenementModel;

    }

    //recuperer les etudiants du club participant a un event
    public List<EtudiantModel> getEtudiantsParticipating(int idEvenement){
        EvenementModel evenementModel=evenementRepository.findById(idEvenement).get();
        List<ClubModel> participants=evenementModel.getParticipants();
        List<EtudiantModel> etudiants=new ArrayList<>();
        for(ClubModel club:participants){
            etudiants.addAll(club.getEtudiantModelList());
        }
        return etudiants;
    }


    // le nombre des événements par un club specifique (Dashboard)
    @Autowired private ClubRepository clubRepository;
    @Transactional
    public int countEventsByClubName(String clubName) {
        ClubModel club = clubRepository.findClubModelByLibelle(clubName);
        if (club != null) {
            int countEvents = evenementRepository.countByParticipants(club);
            System.out.println("countEvents : " + countEvents);
            return countEvents;
        } else {
            throw new EntityNotFoundException("club not found !! , club name : " + clubName);
        }
    }

    // recuperer event par nom
    // pour générer la liste des clubs ayant confirmé la paraticipation a un event
    @Transactional
    public EvenementModel getEventByLibelle(String eventName){
        EvenementModel evenement = evenementRepository.findByLibelle(eventName);
        if(evenement!=null)
            return modelMapper.map(evenement, EvenementModel.class);
        else
            throw new EntityNotFoundException("Event not found ! name : " + eventName);
    }



    private EvenementModel evenementModel;
    public void genererListClubsParEvent(String eventLibelle, HttpServletResponse response) throws IOException {

        evenementModel = getEventByLibelle(eventLibelle);

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        // TITLE
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        titleFont.setSize(18);
        titleFont.setColor(Color.BLACK);

        Paragraph title = new Paragraph("Rapport d'événement: "+evenementModel.getLibelle(), titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        // FONT
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        headerFont.setSize(10);
        headerFont.setColor(CMYKColor.BLACK);
        // TABLE
        PdfPTable table = new PdfPTable(7); // 7 column :clubs, libelle, date_debut, date_fin, type, budget, status
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        // HEADER CELLS
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(CMYKColor.LIGHT_GRAY);
        cell.setPadding(5);
        // HEADER COLUMNS
        cell.setPhrase(new Phrase("Clubs", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Evenement", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Date début", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Date fin", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Type", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Budget", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Status", headerFont));
        table.addCell(cell);

        List<ClubModel> participants = evenementModel.getParticipants();
        int numParticipants = participants.size();

// Add rows for each participant
        for (int i = 0; i < numParticipants; i++) {
            ClubModel participant = participants.get(i);
            cell = new PdfPCell(new Phrase(participant.getLibelle()));
            table.addCell(cell);

            if (i == 0) {
                cell = new PdfPCell(new Phrase(evenementModel.getLibelle()));
                cell.setRowspan(numParticipants);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(evenementModel.getDate_debut().toString()));
                cell.setRowspan(numParticipants);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(evenementModel.getDate_fin().toString()));
                cell.setRowspan(numParticipants);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(evenementModel.getType()));
                cell.setRowspan(numParticipants);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(evenementModel.getBudget())));
                cell.setRowspan(numParticipants);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(evenementModel.getStatut().toString()));
                cell.setRowspan(numParticipants);
                table.addCell(cell);
            } else {
                table.addCell("");
                table.addCell("");
                table.addCell("");
                table.addCell("");
                table.addCell("");
                table.addCell("");
            }
        }

        document.add(table);
        document.close();
    }

}