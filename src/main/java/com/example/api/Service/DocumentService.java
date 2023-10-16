package com.example.api.Service;

import com.example.api.Models.*;
import com.example.api.Repository.*;
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
    @Autowired private AdminRepository adminRepository;

    @Transactional                                      // UuserEmail TO BE REPLACED BY USER ROLE
    public DocumentModel uploadFile(MultipartFile file, String userEmail ,String libelle, int selectedClubID) throws FileSystemException {
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

            Optional<ReferentAcademiqueModel> ref = Optional.ofNullable(referentRepository.findByEmail(userEmail));
            Optional<AdminModel> admin = Optional.ofNullable(adminRepository.findByEmail(userEmail));
            Optional<EtudiantModel> etudiant = Optional.ofNullable(etudiantRepository.findByEmail(userEmail));
            Optional<ClubModel> club = clubRepository.findById(selectedClubID);

            if (ref.isPresent() && userEmail.equals(ref.get().getEmail()) && club.isPresent()) {
                document.setReferent(ref.get());
                document.setClub(club.get());
            }
            if (etudiant.isPresent() && userEmail.equals(etudiant.get().getEmail()) && club.isPresent()) {
                document.setEtudiant(etudiant.get());
                document.setClub(club.get());
            } else if (admin.isPresent() && userEmail.equals(admin.get().getEmail())) {
                document.setAdminModel(admin.get());
            }

            document.setLibelle(libelle);
            //document.setClubList(document.getClubList());
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
            throw new RuntimeException(e);
        } catch (Exception ex) {
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

    @Autowired private EtudiantRepository etudiantRepository;

    public List<DocumentModel> getDocumentsByEtudiantClub(int idEtudiant) {
        Optional<EtudiantModel> optionalEtudiantID = etudiantRepository.findById(idEtudiant);

        if (optionalEtudiantID.isPresent()) {
            EtudiantModel etd = optionalEtudiantID.get();
            List<DocumentModel> docs = new ArrayList<>();
            for (ClubModel club : etd.getClubModelList()) {
                docs.addAll(club.getDocumentModelList());
            }
            return docs;
        }
        throw new EntityNotFoundException("User exists but doesn't have the expected role.");
    }


    public List<DocumentModel> getDocumentsByReferentClub(int idReferent){
        Optional<ReferentAcademiqueModel> optionalReferentID = referentRepository.findById(idReferent);
        if (optionalReferentID.isPresent()) {
            ReferentAcademiqueModel ref = optionalReferentID.get();
            List<DocumentModel> docs = new ArrayList<>();
            for (ClubModel club : ref.getClubModelList()) {
                docs.addAll(club.getDocumentModelList());
            }
            return docs;
        }else{
            System.out.println("No matching referent");
            throw new EntityNotFoundException("referent doesnt exists");
        }

    }


/*
    public List<DocumentModel> getDocumentsByUserClub(int idUser, String role) {
        Optional<ReferentAcademiqueModel> optionalReferentID = referentRepository.findById(idUser);
        Optional<EtudiantModel> optionalEtudiantID = etudiantRepository.findById(idUser);

        if (optionalEtudiantID.isPresent()) {
            EtudiantModel etd = optionalEtudiantID.get();
            if (etd.getFkrole() != null) {
                String etdRole = etd.getFkrole().getLibelle();

                if (etdRole.equals(role)) {
                    List<DocumentModel> docs = new ArrayList<>();
                    for (ClubModel club : etd.getClubModelList()) {
                        docs.addAll(club.getDocumentModelList());
                    }
                    return docs;
                }
            }
        }

        if (optionalReferentID.isPresent() && role == null) {
            ReferentAcademiqueModel ref = optionalReferentID.get();
            List<DocumentModel> docs = new ArrayList<>();
            for (ClubModel club : ref.getClubModelList()) {
                docs.addAll(club.getDocumentModelList());
            }
            return docs;
        }

        // Use a logger for better logging in production.
        System.out.println("No matching user or role.");
        throw new EntityNotFoundException("User exists but doesn't have the expected role.");
    }
*/



}
