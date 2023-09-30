package com.example.api.Service;

import com.example.api.Models.ClubModel;
import com.example.api.Models.DocumentModel;
import com.example.api.Models.ReferentAcademiqueModel;
import com.example.api.Repository.ClubRepository;
import com.example.api.Repository.DocumentRepository;
import com.example.api.Repository.ReferentAcademiqueRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Yassine
 * @project API
 */

@Service
@Data
public class DocumentService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ReferentAcademiqueRepository referentRepository;

    @Transactional
    public DocumentModel uploadFile(MultipartFile file, int idReferent, String libelle) throws FileSystemException {
        DocumentModel document = new DocumentModel();
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileType = file.getContentType();

            java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = new Date(sqlDate.getTime());
            document.setDateEnvoi(date);

            if (fileName.contains("..")) // Check if the file's name contains invalid characters
                throw new FileSystemException("Sorry! Filename contains invalid path sequence " + fileName);
            document.setFileName(fileName);
            document.setFileType(fileType);
            document.setFile(file.getBytes());
            Optional<ReferentAcademiqueModel> ref = referentRepository.findById(idReferent);
            if (ref.isPresent())
                document.setReferent(ref.get());
            document.setLibelle(libelle);
            //document.setClubList(document.getClubList());
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
            throw new RuntimeException(e);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
        }
        document = documentRepository.save(document);
        return modelMapper.map((Object) document, (Type) DocumentModel.class);
    }

    /**
     * FIND ALL FILES
     *
     * @return
     */
    @Transactional
    public List<DocumentModel> findAll() {
        return documentRepository.findAll().stream().map(element -> modelMapper.map(element, DocumentModel.class))
                .collect(Collectors.toList());
    }

    /**
     * GET FILES BY CLUB
     *
     * @param libelle
     * @return
     */
    @Transactional
    public List<DocumentModel> getAllFilesByClubName(String libelle) {
        ClubModel club = clubRepository.findClubModelByLibelle(libelle);
        if (club == null) {
            throw new EntityNotFoundException("This club does not exist : " + libelle);
        }
        return documentRepository.findDocumentModelByReferent_ClubModelList(club)
                .stream()
                .map(element -> modelMapper.map(element, DocumentModel.class))
                .collect(Collectors.toList());
    }


    /**
     * GET ONE FILE BY NAME
     *
     * @param fileName
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public DocumentModel getFileByName(String fileName) throws EntityNotFoundException {
        return modelMapper.map(documentRepository.findDocumentByFileName(fileName), DocumentModel.class);
    }

    /**
     * GET FILE BY ID
     *
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public DocumentModel getFileByID(int id) throws EntityNotFoundException {
        return modelMapper.map(documentRepository.findById(id), DocumentModel.class);
    }

    /**
     * GET FILE BY DATE ENVOI
     *
     * @param dateEnvoi
     * @return
     */
    @Transactional
    public List<DocumentModel> getByDateEnvoi(Date dateEnvoi) {
        return documentRepository.findDocumentByDateEnvoi(dateEnvoi)
                .stream().map(element -> modelMapper.map(element, DocumentModel.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public DocumentModel deleteFileByID(int id) {
        Optional<DocumentModel> document = documentRepository.findById(id);
        if (document.isPresent()) {
            documentRepository.deleteById(id);
            return modelMapper.map(document, DocumentModel.class);
        } else {
            throw new EntityNotFoundException("Document not found, id= " + id);
        }
    }


}
