package com.example.api.Service;

import com.example.api.Models.*;
import com.example.api.Repository.ClubRepository;
import com.example.api.Repository.EtudiantRepository;
import com.example.api.Repository.ReferentAcademiqueRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.hibernate.Hibernate;
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

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Yassine&Youssef
 * @project Gestion-club-uir
 */

@Data
@Service
public class ClubService {
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * SAVE CLUB
     *
     * @param clubModel
     * @return
     */
    @Transactional
    public ClubModel saveClub(ClubModel clubModel) {
        clubModel.setType(clubModel.getType());
        clubModel.setLibelle(clubModel.getLibelle());
        clubModel.setEtudiantModelList(clubModel.getEtudiantModelList());
        ClubModel club = clubRepository.save(clubModel);
        return modelMapper.map(club, ClubModel.class);
    }

    /**
     * UPDATE CLUB
     *
     * @param
     * @param id
     * @return
     */
    @Transactional
    public ClubModel updateClub(ClubModel clubModel, int id) {
        Optional<ClubModel> clubModelOptional = clubRepository.findById(id);
        if (clubModelOptional.isPresent()) {
            ClubModel existingClub = clubModelOptional.get();
            List<EtudiantModel> originalEtudiantList = existingClub.getEtudiantModelList(); // Store the original list
            List<DocumentModel> originalDocumentList = existingClub.getDocumentModelList();
            List<ReunionModel> Reunions = existingClub.getReunionModel();

            existingClub.setLibelle(clubModel.getLibelle());
            existingClub.setType(clubModel.getType());
            existingClub.setStatut(clubModel.getStatut());
            existingClub.setDescription(clubModel.getDescription());

            // Set the etudiantModelList back to its original value
            existingClub.setEtudiantModelList(originalEtudiantList);
            existingClub.setReunionModel(Reunions);
            existingClub.setDocumentModelList(originalDocumentList);

            ClubModel updated = clubRepository.save(existingClub);
            return modelMapper.map(updated, ClubModel.class);
        } else {
            System.out.println("This club does not exist!");
            return null;
        }
    }




    /**
     * GET ALL CLUBS
     *
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional(readOnly = true)
    public List<ClubModel> findAll() throws EntityNotFoundException {
        return clubRepository.findAll().stream().map(element -> modelMapper.map(element, ClubModel.class))
                .collect(Collectors.toList());
    }

    /**
     * GET CLUB BY ID
     *
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public ClubModel findById(int id) throws EntityNotFoundException {
        try {
            return modelMapper.map(clubRepository.findById(id), ClubModel.class);
        } catch (EntityNotFoundException exception) {
            exception.getMessage();
            System.out.println("Club not found");
            return null;
        }
    }

    /**
     * GET CLUB BY LIBELLE
     *
     * @param Libelle
     * @return
     */
    @Transactional
    public ClubModel findByName(String Libelle) {
        try {
            return modelMapper.map(clubRepository.findClubModelByLibelle(Libelle), ClubModel.class);
        } catch (EntityNotFoundException exception) {
            exception.getMessage();
            System.out.println("Club  not found by the given name");
            return null;
        }
    }

    /**
     * DELETE CLUB BY ID
     *
     * @param id
     * @return
     */
    @Transactional
    public ClubModel deleteById(int id) {
        Optional<ClubModel> clubModelOptional = clubRepository.findById(id);
        if (clubModelOptional.isPresent()) {
            ClubModel clubModel = clubModelOptional.get();
            Hibernate.initialize(clubModel.getEtudiantModelList());
            Hibernate.initialize(clubModel.getDocumentModelList());
            clubRepository.deleteById(id);
            return modelMapper.map(clubModelOptional, ClubModel.class);
        } else {
            System.out.println("this club does not exist !");
            throw new EntityNotFoundException();
        }
    }

    /**
     * DELETE ALL CLUBS
     *
     * @return
     */
    @Transactional
    public ClubModel deleteAll() {
        List<ClubModel> clubModelList = clubRepository.findAll();
        if (!clubModelList.isEmpty()) {
            clubRepository.deleteAll();
            return modelMapper.map(clubModelList, ClubModel.class);
        } else {
            System.out.println("this club does not exist !");
            throw new EntityNotFoundException();
        }
    }

    /**
     * GET ALL PARTICIPANTS OF A SPECIFIC CLUB
     *
     * @param id
     * @return
     */
    @Transactional
    public List<EtudiantModel> getStudentsInClub(int id) { //get students in one club
        Optional<ClubModel> clubModelOptional = clubRepository.findById(id);
        if (!clubModelOptional.isPresent()) {
            throw new EntityNotFoundException("ClubController not found with id " + id);
        }
        ClubModel clubModel = clubModelOptional.get();
        List<EtudiantModel> etudiantModelList = clubModel.getEtudiantModelList().stream()
                .map(student -> modelMapper.map(student, EtudiantModel.class))
                .collect(Collectors.toList());
        return etudiantModelList;
    }

    //Injection EtudiantRepository
    @Autowired
    private EtudiantRepository etudiantRepository;

    //Envoyer une demande de création de club
    @Transactional
    public ClubModel EnvoyerDem(int idEtd, ClubModel clubModel) {
        EtudiantModel etudiantModel = etudiantRepository.findById(idEtd).get();
        clubModel.setType(clubModel.getType());
        clubModel.setLibelle(clubModel.getLibelle());
        clubModel.setDescription(clubModel.getDescription());
        if (clubModel.getEtudiantModelList() == null) {
            clubModel.setEtudiantModelList(new ArrayList<>()); // initialisation de la liste
        }
        clubModel.getEtudiantModelList().add(etudiantModel);
        clubModel.setReferent(clubModel.getReferent());
        //clubModel.setStatus(String.valueOf(ClubStatut.en_attente));
        clubModel.setStatut(ClubStatut.en_attente);
        ClubModel club = clubRepository.save(clubModel);
        return modelMapper.map(club, ClubModel.class);
    }

    //Get Club by status:
    @Transactional
    public List<ClubModel> getClubByStatus(ClubStatut clubStatut) throws EntityNotFoundException {
        return clubRepository.findClubModelByStatut(clubStatut).stream().map(element -> modelMapper.map(element, ClubModel.class))
                .collect(Collectors.toList());
    }
    //filter accepted clubs list where the user doesn't exist for a user to join a new club
    @Transactional
    public List<ClubModel> getClubsAccepteWhereUserNotExists(int idEtudiant) throws EntityNotFoundException {
        return clubRepository.findClubsByStatutWhereUserDoesNotExist(idEtudiant).stream().map(element -> modelMapper.map(element, ClubModel.class))
                .collect(Collectors.toList());
    }

    //Valider demande de création de club
    @Transactional
    public ClubModel ValiderDemandeCreation(int idClub) {
        ClubModel clubModel = clubRepository.findById(idClub).orElseThrow(() -> new EntityNotFoundException("Club not found with id " + idClub));
        clubModel.setStatut(ClubStatut.accepte);
        ClubModel ClubValidated = clubRepository.save(clubModel);
        return modelMapper.map(ClubValidated, ClubModel.class);

    }

    // delete student from club
    @Transactional
    public ClubModel deleteStudentFromClub(String clubName, int idStudent) {
        Optional<ClubModel> clubModelOptional = Optional.ofNullable(clubRepository.findClubModelByLibelle(clubName));
        if (clubModelOptional.isEmpty()) {
            throw new EntityNotFoundException("CLub not found");
        }
        ClubModel clubModel = clubModelOptional.get();
        List<EtudiantModel> etudiantModelList = clubModel.getEtudiantModelList();
        EtudiantModel etudiantModelToRemove = etudiantModelList.stream()
                .filter(e -> e.getId_etudiant() == idStudent)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Student not found in club : "+clubName));

        etudiantModelList.remove(etudiantModelToRemove);
        clubModel.setEtudiantModelList(etudiantModelList);
        clubRepository.save(clubModel);
        return modelMapper.map(clubModel, ClubModel.class);
    }

    // NOMBRE TOTAL DES CLUBS (POUR DASHBOARD)
    @Transactional
    public int countAllClubs(){
        int totalClubs = (int) clubRepository.count();
        if(totalClubs == 0)
            throw new EntityNotFoundException(" 0 Clubs found !! ");
        else
            return totalClubs;
    }

    // NOMBRE TOTAL DES PARTICIPANTS D'UN CLUB SPECIFIQUE (POUR DASHBOARD)
    public int countClubParticipants(String clubName) {
        Optional<ClubModel> clubModel = Optional.ofNullable(clubRepository.findClubModelByLibelle(clubName));
        if (clubModel.isPresent()) {
            int count = clubModel.map(club -> club.getEtudiantModelList().size()).orElse(0);
            System.out.println("Count = "+ count);
            return count;
        }else {
            throw new EntityNotFoundException("club not found");
        }
    }
        @Autowired private ReferentAcademiqueRepository referentRepository;

    public List<ClubModel> getClubListByReferent(int idReferent) {
        Optional<ReferentAcademiqueModel> optionalReferent = referentRepository.findById(idReferent);

        if (optionalReferent.isPresent()) {
            ReferentAcademiqueModel referent = optionalReferent.get();
            return referent.getClubModelList();
        } else {
            throw new EntityNotFoundException("this referent does not exist !"+ idReferent);
        }
    }
    public List<ClubModel> getClubListByEtudiant(int idEtudiant) {
        Optional<EtudiantModel> optionalEtudiant = etudiantRepository.findById(idEtudiant);

        if (optionalEtudiant.isPresent()) {
            EtudiantModel etudiant = optionalEtudiant.get();
            return etudiant.getClubModelList();
        } else {
            throw new EntityNotFoundException("this etudiant does not exist !"+ idEtudiant);
        }
    }


    /**
     * upload club logo
     */

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "\\ClubsUIR data\\uploads\\Clubs\\logos\\";

    public String saveCLubLogo(MultipartFile file, int idClub) throws FileSystemException {

        ImageModel imageModelFile = new ImageModel();
        ClubModel clubModel = clubRepository.findById(idClub)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with ID: " + idClub));

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
            imageModelFile.setClubLogo(file.getBytes());

            clubModel.setClubLogoPath(imageModelFile.getFilePath());
            clubModel.setClubLogoName(imageModelFile.getFileName());
            clubRepository.save(clubModel);
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
            throw new RuntimeException(e);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException)
                throw new RuntimeException("A file of that name already exists.");
        }
        return imageModelFile.getFilePath();
    }

    public ResponseEntity<Resource> getClubLogo(int idClub) throws IOException {
        Optional<ClubModel> clubOptional = clubRepository.findById(idClub);

        if (clubOptional.isPresent()) {
            ClubModel club = clubOptional.get();
            String imageName = club.getClubLogoName();

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




}

