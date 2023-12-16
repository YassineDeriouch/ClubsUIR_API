package com.example.api.Controller;

import com.example.api.Models.DocumentModel;
import com.example.api.Models.DocumentsAdminResponseDTO;
import com.example.api.Models.ResponseData;
import com.example.api.Service.DocumentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.FileSystemException;
import java.util.List;
import java.util.Objects;

/**
 * @author Yassine
 * @project API
 */
@Data
@CrossOrigin(origins="*")
@RequiredArgsConstructor
@RestController
@RequestMapping("document")
@Tag(name = "Document", description = "Documents management API")
public class DocumentController {

    private final DocumentService documentService;
    public static String fn, ft;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/upload/file/user/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<DocumentModel> uploadDocument(@RequestPart(value = "file") MultipartFile file,
                                                        @RequestParam String userEmail,
                                                        @RequestParam String libelle,
                                                        @RequestParam int selectedClubID) {
        try {
            System.out.println("Filename = " + file.getOriginalFilename() + "filetype= " + file.getContentType());
            String downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
                    .path(Objects.requireNonNull(file.getOriginalFilename())).toUriString();
            System.out.println("downloadURl: "+downloadURl);
            new ResponseData(file.getOriginalFilename(), downloadURl, file.getContentType(), file.getSize());
            System.out.println("Uploaded the file successfully: " + file.getOriginalFilename());

            return new ResponseEntity<>(documentService.uploadFile(file, userEmail, libelle, selectedClubID), HttpStatus.OK);
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET FILE BY ID
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/get/id")
    public ResponseEntity<DocumentModel> downloadDocument(@RequestParam int id){
        return new ResponseEntity<>(documentService.getFileByID(id), HttpStatus.OK);
    }

    @CrossOrigin(origins="*")
    @GetMapping("/get/all")
    public ResponseEntity<List<DocumentModel>> getAllFiles() {
        List<DocumentModel> documents = documentService.findAll();
        return ResponseEntity.ok().body(documents);
    }

    @CrossOrigin(origins="*")
    @GetMapping("/download/file/idFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam int id) throws Exception {
        try {
            DocumentModel document = documentService.getFileByID(id);
            return  ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(document.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + document.getFileName()
                                    + "\"")
                    .body(new ByteArrayResource(document.getFile()));
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping("/get/files/Club")
    public ResponseEntity<List<DocumentModel>> getAllFilesByCLUB(@RequestParam String clubName){
        try{
            return new ResponseEntity<>(documentService.getAllFilesByClubName(clubName), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/files/club/byEtudiant={idEtudiant}")
    public ResponseEntity<List<DocumentModel>> getDocumentsByEtudiantClub(@PathVariable int idEtudiant){
        try{
            return new ResponseEntity<>(documentService.getDocumentsByEtudiantClub(idEtudiant), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/get/files/club/byReferent={idReferent}")
    public ResponseEntity<List<DocumentModel>> getDocumentsByReferentClub(@PathVariable int idReferent ){
        try{
            return new ResponseEntity<>(documentService.getDocumentsByReferentClub(idReferent), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/get/files/byAdmin")
    public ResponseEntity<List<DocumentsAdminResponseDTO>> getDocumentsByAdmin() throws EntityNotFoundException{
        return new ResponseEntity<>(documentService.getDocumentsByAdmin(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/delete/id")
    public ResponseEntity<DocumentModel> deleteFileByID(@RequestParam int idFile){
        try {
            return new ResponseEntity<>(documentService.deleteFileByID(idFile), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}











