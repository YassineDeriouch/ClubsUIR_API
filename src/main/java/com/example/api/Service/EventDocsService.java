package com.example.api.Service;

import com.example.api.Models.ClubModel;
import com.example.api.Models.DocumentModel;
import com.example.api.Models.EventDocsModel;
import com.example.api.Models.ReferentAcademiqueModel;
import com.example.api.Repository.ClubRepository;
import com.example.api.Repository.DocumentRepository;
import com.example.api.Repository.EventDocsRepository;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author Youssef
 * @project API
 */

@Service
@Data
public class EventDocsService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EventDocsRepository eventDocsRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ReferentAcademiqueRepository referentRepository;

    @Transactional
    public EventDocsModel uploadFile(MultipartFile file, String libelle) throws FileSystemException {
        EventDocsModel eventDocsModel = new EventDocsModel();
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileType = file.getContentType();

            Date sqlDate = new Date(System.currentTimeMillis());
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = new Date(sqlDate.getTime());
            eventDocsModel.setDateEnvoi(date);

            if (fileName.contains("..")) // Check if the file's name contains invalid characters
                throw new FileSystemException("Sorry! Filename contains invalid path sequence " + fileName);
            eventDocsModel.setFileName(fileName);
            eventDocsModel.setFileType(fileType);
            eventDocsModel.setFile(file.getBytes());
            eventDocsModel.setLibelle(libelle);
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
            throw new RuntimeException(e);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
        }
        eventDocsModel = eventDocsRepository.save(eventDocsModel);
        return modelMapper.map((Object) eventDocsModel, (Type) EventDocsModel.class);
    }

    /**
     * FIND ALL FILES
     *
     * @return
     */
    @Transactional
    public List<EventDocsModel> findAll() {
        return eventDocsRepository.findAll().stream().map(element -> modelMapper.map(element, EventDocsModel.class))
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
    public EventDocsModel getFileByName(String fileName) throws EntityNotFoundException {
        return modelMapper.map(eventDocsRepository.findDocumentByFileName(fileName), EventDocsModel.class);
    }

    /**
     * GET FILE BY ID
     *
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public EventDocsModel getFileByID(int id) throws EntityNotFoundException {
        return modelMapper.map(eventDocsRepository.findById(id), EventDocsModel.class);
    }

    /**
     * GET FILE BY DATE ENVOI
     *
     * @param dateEnvoi
     * @return
     */
    @Transactional
    public List<EventDocsModel> getByDateEnvoi(Date dateEnvoi) {
        return eventDocsRepository.findDocumentByDateEnvoi(dateEnvoi)
                .stream().map(element -> modelMapper.map(element, EventDocsModel.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public EventDocsModel deleteFileByID(int id) {
        Optional<EventDocsModel> document = eventDocsRepository.findById(id);
        if (document.isPresent()) {
            eventDocsRepository.deleteById(id);
            return modelMapper.map(document, EventDocsModel.class);
        } else {
            throw new EntityNotFoundException("Document not found, id= " + id);
        }
    }


}
