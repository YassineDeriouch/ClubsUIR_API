package com.example.api.Service;
import com.example.api.Models.*;
import com.example.api.Repository.ClubRepository;
import com.example.api.Repository.EtudiantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;
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

    public boolean findEtudiantModelByEmail(String email){
        Optional<EtudiantModel> etudiantopt= Optional.ofNullable(etudiantRepository.findByEmail(email));
        if(etudiantopt.isPresent()){
        return true;
        }
        else{
            return false;
        }
    }

    public boolean findEtudiantModelByPassword(String password){
        Optional<EtudiantModel> etudiantopt= Optional.ofNullable(etudiantRepository.findByPassword(password));
        if(etudiantopt.isPresent()){
        return true;
        }
        else{
            return false;
        }
    }
    public Map<String, List<EtudiantModel>> ListParticipantOfHisClub(int idEtd) {
        Optional<EtudiantModel> opt_etd = etudiantRepository.findById(idEtd);
        Map<String, List<EtudiantModel>> clubParticipants = new HashMap<>();

        if (opt_etd.isPresent()) {
            EtudiantModel etd = opt_etd.get();
            for (ClubModel c : etd.getClubModelList()) {
                String clubName = c.getLibelle();
                List<EtudiantModel> participants = c.getEtudiantModelList();
                clubParticipants.put(clubName, participants);
            }
            return clubParticipants;
        } else {
            throw new IllegalStateException("Error");
        }
    }

    public List<ReunionModel> GetReunionByPresidentClubs(int idEtd){
        Optional<EtudiantModel>opt_etd=etudiantRepository.findById(idEtd);
        List<ReunionModel> reunionModelList=new ArrayList<>();
        if(opt_etd.isPresent()){
            EtudiantModel etd=opt_etd.get();
            System.out.println("etd role ===> "+ etd.getFkrole().getLibelle());
            boolean pres =  etd.getFkrole().getLibelle().equals("president");
            boolean vice =  etd.getFkrole().getLibelle().equals("vice-president");
            boolean secretaire =  etd.getFkrole().getLibelle().equals("secretaire");
            boolean tresorier =  etd.getFkrole().getLibelle().equals("tresorier");
            if(pres || vice || secretaire || tresorier){
               for(ClubModel c:etd.getClubModelList()){
                        reunionModelList.addAll(c.getReunionModel());
                    }
                    return reunionModelList;
            }
            else {
                throw new IllegalStateException("You are not a president");
            }
        }
        else {
            throw new IllegalStateException("Error");
        }
    }

    ///////////:---------- upload and get referent profile picture -------:////////

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "\\ClubsUIR data\\uploads\\Etudiant\\profile picture\\";

    public String saveEtudiantProfilePicture(MultipartFile file, int idEtudiant) throws FileSystemException {

        ImageModel imageModelFile = new ImageModel();
        EtudiantModel etudiantModel = etudiantRepository.findById(idEtudiant)
                .orElseThrow(() -> new EntityNotFoundException("etudiant not found with ID: " + idEtudiant));

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
            imageModelFile.setClubLogo(file.getBytes());

            etudiantModel.setEtudiantProfilePicturePath(imageModelFile.getFilePath());
            etudiantModel.setEtudiantProfilePictureName(imageModelFile.getFileName());
            etudiantRepository.save(etudiantModel);
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
            throw new RuntimeException(e);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException)
                throw new RuntimeException("A file of that name already exists.");
        }
        return imageModelFile.getFilePath();
    }

    public ResponseEntity<Resource> getEtudiantProfilePicture(int idEtudiant) throws IOException {
        Optional<EtudiantModel> etudiantOptional = etudiantRepository.findById(idEtudiant);

        if (etudiantOptional.isPresent()) {
            EtudiantModel etudiant = etudiantOptional.get();
            String imageName = etudiant.getEtudiantProfilePictureName();

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
