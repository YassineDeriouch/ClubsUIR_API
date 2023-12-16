package com.example.api.Service;

import com.example.api.Models.*;
import com.example.api.Models.ReferentAcademiqueModel;
import com.example.api.Repository.ReferentAcademiqueRepository;
import jakarta.persistence.EntityNotFoundException;
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

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
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
    public ReferentAcademiqueModel updateReferent(ReferentAcademiqueModel referent, int id) throws EntityNotFoundException {
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
     *
     * @return
     */
    @Transactional
    public List<ReferentAcademiqueModel> getAllReferents() throws EntityNotFoundException {
        return referentRepository.findAll().stream().map(element -> modelMapper.map(element, ReferentAcademiqueModel.class))
                .collect(Collectors.toList());
    }

    /**
     * GET BY ID
     *
     * @Param id
     */
    @Transactional
    public ReferentAcademiqueModel getReferentByID(int id) throws EntityNotFoundException {
        return modelMapper.map(referentRepository.findById(id), ReferentAcademiqueModel.class);
    }


    /**
     * DELETE ROLE BY ID
     *
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public ReferentAcademiqueModel deleteByID(int id) throws EntityNotFoundException {
        Optional<ReferentAcademiqueModel> roleOptional = referentRepository.findById(id);
        if (roleOptional.isPresent()) {
            referentRepository.deleteById(id);
            return modelMapper.map(roleOptional, ReferentAcademiqueModel.class);
        } else {
            System.out.println();
            throw new EntityNotFoundException("this referent does not exist !");
        }
    }

    /**
     * DELETE ALL ROLES BY ID
     *
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public ReferentAcademiqueModel deleteAll() throws EntityNotFoundException {
        List<ReferentAcademiqueModel> referentList = referentRepository.findAll();
        if (!referentList.isEmpty()) {
            referentRepository.deleteAll();
            return modelMapper.map(referentList, ReferentAcademiqueModel.class);
        } else {
            throw new EntityNotFoundException("The referent list is empty");
        }
    }

    public boolean findReferentAcademiqueModelByEmail(String email) {
        Optional<ReferentAcademiqueModel> referentAcademiqueModel = Optional.ofNullable(referentRepository.findByEmail(email));

        if (referentAcademiqueModel.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean findReferentAcademiqueModelByPassword(String password) {
        Optional<ReferentAcademiqueModel> referentAcademiqueModel = Optional.ofNullable(referentRepository.findByPassword(password));

        if (referentAcademiqueModel.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public List<EtudiantModel> ListStudentsInHisClub(int idReferent) {
        Optional<ReferentAcademiqueModel> optref = referentRepository.findById(idReferent);

        if (optref.isPresent()) {
            ReferentAcademiqueModel ref = optref.get();
            List<ClubModel> clubs = ref.getClubModelList();
            List<EtudiantModel> etudiants = new ArrayList<>();
            for (ClubModel club : clubs) {
                etudiants.addAll(club.getEtudiantModelList());
            }
            return etudiants;
        } else {
            throw new EntityNotFoundException("this referent does not exist !");
        }
    }

    public Map<String, List<EtudiantModel>> GetParticipantsInMeetingsByClubReferent(int idReferent) {
        Optional<ReferentAcademiqueModel> opt_ref = referentRepository.findById(idReferent);
        Map<String, List<EtudiantModel>> clubStudentsMap = new HashMap<>();

        if (opt_ref.isPresent()) {
            ReferentAcademiqueModel ref = opt_ref.get();

            for (ClubModel club : ref.getClubModelList()) {
                List<EtudiantModel> etudiants = new ArrayList<>();
                for (ReunionModel reunion : club.getReunionModel()) {
                    for (ClubModel subClub : reunion.getListClubs()) {
                        etudiants.addAll(subClub.getEtudiantModelList());
                    }
                }
                clubStudentsMap.put(club.getLibelle(), etudiants);
            }
        } else {
            throw new EntityNotFoundException("This referent does not exist!");
        }

        return clubStudentsMap;
    }

    public List<ReunionModel> ListReunionsOfHisClub(int idReferent) {
        Optional<ReferentAcademiqueModel> optref = referentRepository.findById(idReferent);

        if (optref.isPresent()) {
            ReferentAcademiqueModel ref = optref.get();
            List<ReunionModel> reunions = new ArrayList<>();
            for (ClubModel club : ref.getClubModelList()) {
                reunions.addAll(club.getReunionModel());
            }
            return reunions;
        } else {
            throw new EntityNotFoundException("this referent does not exist !");
        }
    }

    ///////////:---------- upload and get referent profile picture -------:////////

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "\\ClubsUIR data\\uploads\\Referent\\profile picture\\";

    public String saveReferentProfilePicture(MultipartFile file, int idReferent) throws FileSystemException {

        ImageModel imageModelFile = new ImageModel();
        ReferentAcademiqueModel referentModel = referentRepository.findById(idReferent)
                .orElseThrow(() -> new EntityNotFoundException("referent not found with ID: " + idReferent));

        try{
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileType = file.getContentType();

            List<String> allowedFileExtensions = Arrays.asList("jpg", "jpeg", "png");

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

            referentModel.setReferentProfilePicturePath(imageModelFile.getFilePath());
            referentModel.setReferentProfilePictureName(imageModelFile.getFileName());
            referentRepository.save(referentModel);
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
            throw new RuntimeException(e);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException)
                throw new RuntimeException("A file of that name already exists.");
        }
        return imageModelFile.getFilePath();
    }

    public ResponseEntity<Resource> getReferentProfilePicture(int idReferent) throws IOException {
        Optional<ReferentAcademiqueModel> referentOptional = referentRepository.findById(idReferent);

        if (referentOptional.isPresent()) {
            ReferentAcademiqueModel referent = referentOptional.get();
            String imageName = referent.getReferentProfilePictureName();

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
