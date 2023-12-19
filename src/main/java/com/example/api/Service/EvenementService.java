package com.example.api.Service;

import com.example.api.Models.*;
import com.example.api.Repository.*;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
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
    @Autowired private ReferentAcademiqueRepository referentRepository;
    @Autowired private AdminRepository adminRepository;


    //Send demand to create an event
    @Autowired private EtudiantService etudiantService;
    public EvenementModel EnvoyerDem(int idEtd, EvenementModel evenementModel){
        EtudiantModel etudiantModel= etudiantService.getStudentById(idEtd);
        addEventHelper(evenementModel);
        evenementModel.setStatut(EvenementStatut.en_attente);
        EvenementModel evenement = evenementRepository.save(evenementModel);
        return modelMapper.map(evenement, EvenementModel.class);
    }

    public void addEventHelper(EvenementModel evenementModel){
        evenementModel.setId_evenement(evenementModel.getId_evenement());
        evenementModel.setLibelle(evenementModel.getLibelle());
        evenementModel.setDate_debut(evenementModel.getDate_debut());
        evenementModel.setDate_fin(evenementModel.getDate_fin());
        evenementModel.setType(evenementModel.getType());
        evenementModel.setBudget(evenementModel.getBudget());
        evenementModel.setDescription(evenementModel.getDescription());
        evenementModel.setParticipants(evenementModel.getParticipants());
    }
    public EvenementModel saveEvent(EvenementModel evenementModel,int selectedClubID){
        Optional<ClubModel> club = clubRepository.findById(selectedClubID);
            addEventHelper(evenementModel);
            evenementModel.setParticipants(Collections.singletonList(club.get()));
            evenementModel.setStatut(EvenementStatut.accepte);
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


   public List<ClubModel> getClubsByEvent(int idEvent){
        Optional<EvenementModel> optionalEvenement=evenementRepository.findById(idEvent);

        if(optionalEvenement.isPresent()){
            EvenementModel evenementModel=optionalEvenement.get();
            return evenementModel.getParticipants();
        }
        else
            throw new EntityNotFoundException("Event not found ! id : " + idEvent);

    }

    public EvenementModel CreerEventByAdmin(EvenementModel evenementModel){
        try{
            return evenementRepository.save(evenementModel);
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalStateException("Error while creating event");
        }
    }

    @Autowired private ReferentAcademiqueRepository RefRepo;
    /*public List<EvenementModel> getEventsByReferent(int idReferent) {
        List<EvenementModel> events = new ArrayList<>();

        Optional<ReferentAcademiqueModel> opt_ref = RefRepo.findById(idReferent);
        if (opt_ref.isPresent()) {
            ReferentAcademiqueModel ref = opt_ref.get();
            for (EvenementModel event : evenementRepository.findAll()) {
                for (ClubModel club : event.getParticipants()) {
                    ReferentAcademiqueModel clubReferent = club.getReferent();
                    if (clubReferent != null && clubReferent.equals(ref)) {
                        events.add(event);
                    }
                }
            }
            return events;
        } else {
            throw new EntityNotFoundException("Referent not found! id: " + idReferent);
        }
    }*/

    public List<EvenementModel> getEventsByReferent(int idReferent) {
        List<EvenementModel> events = new ArrayList<>();

        Optional<ReferentAcademiqueModel> opt_ref = RefRepo.findById(idReferent);
        if (opt_ref.isPresent()) {
            List<ClubModel> clubModelList = opt_ref.get().getClubModelList();
            for (ClubModel clubModel : clubModelList) {
                events.addAll(clubModel.getEvenementList());
            }
        } else {
            throw new EntityNotFoundException();
        }
        return events;
    }

    @Autowired private EtudiantRepository eRepo;
    public List<EvenementModel> getEventsByEtudiant(int idEtd) {
        List<EvenementModel> events = new ArrayList<>();

        Optional<EtudiantModel> opt_etd = eRepo.findById(idEtd);
        if (opt_etd.isPresent()) {
            List<ClubModel> clubModelList = opt_etd.get().getClubModelList();
            for (ClubModel clubModel : clubModelList) {
                events.addAll(clubModel.getEvenementList());
            }
        } else {
            throw new EntityNotFoundException();
        }
        return events;
    }

    public EvenementModel UpdateBudgetEvent(int idEvent,double Budget){
        Optional<EvenementModel>opt_budget=evenementRepository.findById(idEvent);

        if(opt_budget.isPresent()){
            EvenementModel evenementModel=opt_budget.get();
            evenementModel.setBudget(Budget);
            return evenementRepository.save(evenementModel);
        }
        else
            throw new EntityNotFoundException("Event not found by id : " + idEvent);
    }

    /**
     * upload club logo
     */

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "\\ClubsUIR data\\uploads\\Events\\images\\";

    public String saveEventImage(MultipartFile file, int idEvent, UriComponentsBuilder uriBuilder) throws FileSystemException {

        ImageModel imageModelFile = new ImageModel();
        EvenementModel evenementModel = evenementRepository.findById(idEvent)
                .orElseThrow(() -> new EntityNotFoundException("event not found with ID: " + idEvent));

        try{
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileType = file.getContentType();

            List<String> allowedFileExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

            for (String extension : allowedFileExtensions) {
                if (fileName.contains("..") ) {
                    throw new FileSystemException("File type not allowed: " + fileName +", file type:" +fileType);
                }
            }

            String filePath = UPLOAD_DIR + fileName;
            System.out.println("filePath===========>>>> " + filePath);

            Path uploadPath = Path.of(UPLOAD_DIR);
            Files.createDirectories(uploadPath);

            Path destPath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), destPath, StandardCopyOption.REPLACE_EXISTING);

            // FILE DATA
            imageModelFile.setFilePath(destPath.toString());
            imageModelFile.setFileName(fileName);
            imageModelFile.setFileType(fileType);
            imageModelFile.setImageFile(file.getBytes());

            evenementModel.setEventImagePath(imageModelFile.getFilePath());
            evenementModel.setEventImageName(imageModelFile.getFileName());

            String fullPath = uriBuilder.path("/evenement/get/image/idEvent=" + idEvent).toUriString();
            evenementModel.setEventImageURL(fullPath);

            System.out.println("eventImageURL: "+ evenementModel.getEventImageURL());

            evenementRepository.save(evenementModel);

        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
            throw new RuntimeException(e);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException)
                throw new RuntimeException("A file of that name already exists.");
        }
        return imageModelFile.getFilePath();
    }

    public ResponseEntity<Resource> getEventImage(int idEvent) throws IOException {
        Optional<EvenementModel> eventOptional = evenementRepository.findById(idEvent);

        if (eventOptional.isPresent()) {
            EvenementModel event = eventOptional.get();
            String imageName = event.getEventImageName();

            Path imagePath = Path.of(UPLOAD_DIR, imageName);
            FileSystemResource resource = new FileSystemResource(imagePath);

            if (resource.exists()) {
                MediaType contentType;
                if (imageName.endsWith(".png")) {
                    contentType = MediaType.IMAGE_PNG;
                }else if (imageName.endsWith(".jpg")) {
                    contentType = MediaType.IMAGE_PNG;
                } else {
                    contentType = MediaType.IMAGE_JPEG;
                }

                return ResponseEntity.ok()
                        .contentType(contentType)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public void generateImageURL(int idEvent, UriComponentsBuilder uriBuilder,EvenementModel evenementModel) {
        String fullPath = uriBuilder.path("/evenement/get/logo/idEvent=" + idEvent).toUriString();
        evenementModel.setEventImageURL(fullPath);
        evenementRepository.save(evenementModel);
    }

}