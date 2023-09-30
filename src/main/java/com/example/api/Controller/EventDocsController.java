package com.example.api.Controller;

import com.example.api.Models.DocumentModel;
import com.example.api.Models.EventDocsModel;
import com.example.api.Models.ResponseData;
import com.example.api.Service.DocumentService;
import com.example.api.Service.EventDocsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author Youssef
 * @project API
 */
@RestController
@Data
@RequestMapping("EventDocument")
@CrossOrigin(origins="*")
public class EventDocsController {

    @Autowired private EventDocsService eventDocsService;
    public static String fn, ft;

    @CrossOrigin
    @PostMapping(value = "/upload/file/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EventDocsModel> uploadDocument(@RequestPart(value = "file") MultipartFile file,
                                                         @RequestParam String libelle) {
        try {
            System.out.println("Filename = " + file.getOriginalFilename() + "filetype= " + file.getContentType());
            String downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
                    .path(Objects.requireNonNull(file.getOriginalFilename())).toUriString();
            System.out.println("downloadURl: "+downloadURl);
            new ResponseData(file.getOriginalFilename(), downloadURl, file.getContentType(), file.getSize());
            System.out.println("Uploaded the file successfully: " + file.getOriginalFilename());

            return new ResponseEntity<>(eventDocsService.uploadFile(file,libelle), HttpStatus.OK);
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
    @CrossOrigin(origins="*")
    @GetMapping(value = "/get/id")
    public ResponseEntity<EventDocsModel> findDocumentByID(@RequestParam int id){
        try {
            return new ResponseEntity<>(eventDocsService.getFileByID(id), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins="*")
    @GetMapping("/get/all")
    public ResponseEntity<List<EventDocsModel>> getAllFiles() {
        try {
            List<EventDocsModel> documents = eventDocsService.findAll();
            return ResponseEntity.ok().body(documents);
        }catch (EntityNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins="*")
    @GetMapping("/download/file/idFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam int id) throws Exception {
        try {
            EventDocsModel document = eventDocsService.getFileByID(id);
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
    @DeleteMapping("/delete/id")
    public ResponseEntity<EventDocsModel> deleteFileByID(@RequestParam int idFile){
        try {
            return new ResponseEntity<>(eventDocsService.deleteFileByID(idFile), HttpStatus.OK);
        }catch (EntityNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}











