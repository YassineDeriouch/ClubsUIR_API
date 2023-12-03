package com.example.api.Service;

import com.example.api.Models.AdminModel;
import com.example.api.Models.AdminModel;
import com.example.api.Models.ImageModel;
import com.example.api.Repository.AdminRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Yassine Deriouch
 * @project API
 */
@Data
@Service
public class AdminService {

    @Autowired private ModelMapper modelMapper;
    @Autowired private AdminRepository adminRepository;

    /**
     * SAVE ADMIN
     * @param adminModel
     * @return
     */
    @Transactional
    public AdminModel saveAdmin(AdminModel adminModel){
        adminModel.setNom(adminModel.getNom());
        adminModel.setPrenom(adminModel.getPrenom());
        adminModel.setEmail(adminModel.getEmail());
        adminModel.setPassword(adminModel.getPassword());
        adminModel.setTelephone(adminModel.getTelephone());
        AdminModel saved = adminRepository.save(adminModel);
        return modelMapper.map(saved, AdminModel.class);
    }

    /**
     *  UPDATE ADMIN
     * @param idAdmin
     * @param adminModel
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public AdminModel updateAdmin(int idAdmin, AdminModel adminModel) throws EntityNotFoundException {
        Optional<AdminModel> adminOptional = adminRepository.findById(idAdmin);
        if(adminOptional.isPresent()){
           AdminModel adminModel1 =  modelMapper.map(adminModel, AdminModel.class);
           adminModel1.setId_admin(idAdmin);
           AdminModel updated = adminRepository.save(adminModel1);
           return modelMapper.map(updated, AdminModel.class);
        }else{
            throw new EntityNotFoundException("Not found !");
        }
    }

    /**
     * GET ADMIN BY ID
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public AdminModel getAdminByID(int id) throws EntityNotFoundException{
        return modelMapper.map(adminRepository.findById(id), AdminModel.class);
    }

    /**
     * GET ALL ADMINS
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public List<AdminModel> getAllAdmin() throws EntityNotFoundException{
        return adminRepository.findAll().stream().map(element -> modelMapper.map(element, AdminModel.class))
                .collect(Collectors.toList());
    }

    /**
     * DELETE ADMIN BY ID
     * @param id
     * @return
     * @throws Exception
     */
    @Transactional
    public AdminModel deleteAdmin(int id) throws Exception {
        List<AdminModel> admins = adminRepository.findAll();
        if(admins.size() == 1){ // check if there is only one admin
            throw new Exception("Cannot delete admin. Must have at least one admin.");
        }
        Optional<AdminModel> adminOptional = adminRepository.findById(id);
        if(!adminOptional.isPresent()){
            throw new EntityNotFoundException("Admin with id " + id + " not found !");
        }
        adminRepository.deleteById(id);
        return modelMapper.map(adminOptional.get(), AdminModel.class);
    }

    public boolean findAdminModelByEmail(String email){
        Optional<AdminModel> adminModel = Optional.ofNullable(adminRepository.findByEmail(email));
        if(adminModel.isPresent()){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean findAdminModelByPassword(String password){
        Optional<AdminModel> adminModel = Optional.ofNullable(adminRepository.findByPassword(password));
        if(adminModel.isPresent()){
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * upload admin logo
     */

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "\\ClubsUIR data\\uploads\\Admin\\profile picture\\";

    public String saveAdminProfilePicture(MultipartFile file, int idAdmin) throws FileSystemException {

        ImageModel imageModelFile = new ImageModel();
        AdminModel adminModel = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with ID: " + idAdmin));

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

            adminModel.setAdminProfilePicturePath(imageModelFile.getFilePath());
            adminModel.setAdminProfilePictureName(imageModelFile.getFileName());
            adminRepository.save(adminModel);
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
            throw new RuntimeException(e);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException)
                throw new RuntimeException("A file of that name already exists.");
        }
        return imageModelFile.getFilePath();
    }

    public ResponseEntity<Resource> getAdminProfilePicture(int idAdmin) throws IOException {
        Optional<AdminModel> adminOptional = adminRepository.findById(idAdmin);

        if (adminOptional.isPresent()) {
            AdminModel admin = adminOptional.get();
            String imageName = admin.getAdminProfilePictureName();

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
