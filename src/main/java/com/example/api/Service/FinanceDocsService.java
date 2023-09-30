package com.example.api.Service;

import com.example.api.Models.FinanceDocsModel;
import com.example.api.Repository.FinanceDocsRepository;
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
 * @author Yassine Deriouch
 * @project API
 */

@Service
@Data
public class FinanceDocsService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FinanceDocsRepository financeDocsRepository;

    @Transactional
    public FinanceDocsModel uploadFile(MultipartFile file, String libelle) throws FileSystemException {
        FinanceDocsModel financeDocsModel = new FinanceDocsModel();
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileType = file.getContentType();

            Date sqlDate = new Date(System.currentTimeMillis());
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = new Date(sqlDate.getTime());
            financeDocsModel.setDateEnvoi(date);

            if (fileName.contains("..")) // Check if the file's name contains invalid characters
                throw new FileSystemException("Sorry! Filename contains invalid path sequence " + fileName);
            financeDocsModel.setFileName(fileName);
            financeDocsModel.setFileType(fileType);
            financeDocsModel.setFile(file.getBytes());
            financeDocsModel.setLibelle(libelle);
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
            throw new RuntimeException(e);
        } catch (Exception e) {
        }
        financeDocsModel = financeDocsRepository.save(financeDocsModel);
        return modelMapper.map((Object) financeDocsModel, (Type) FinanceDocsModel.class);
    }

    /**
     * FIND ALL FILES
     *
     * @return
     */
    @Transactional
    public List<FinanceDocsModel> findAll() {
        return financeDocsRepository.findAll().stream().map(element -> modelMapper.map(element, FinanceDocsModel.class))
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
    public FinanceDocsModel getFileByName(String fileName) throws EntityNotFoundException {
        return modelMapper.map(financeDocsRepository.findDocumentByFileName(fileName), FinanceDocsModel.class);
    }

    /**
     * GET FILE BY ID
     *
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public FinanceDocsModel getFileByID(int id) throws EntityNotFoundException {
        return modelMapper.map(financeDocsRepository.findById(id), FinanceDocsModel.class);
    }

    /**
     * GET FILE BY DATE ENVOI
     *
     * @param dateEnvoi
     * @return
     */
    @Transactional
    public List<FinanceDocsModel> getByDateEnvoi(Date dateEnvoi) {
        return financeDocsRepository.findDocumentByDateEnvoi(dateEnvoi)
                .stream().map(element -> modelMapper.map(element, FinanceDocsModel.class))
                .collect(Collectors.toList());
    }

    /**
     * DELETE FILE BY ID
     * @param id
     * @return
     */
    @Transactional
    public FinanceDocsModel deleteFileByID(int id) {
        Optional<FinanceDocsModel> document = financeDocsRepository.findById(id);
        if (document.isPresent()) {
            financeDocsRepository.deleteById(id);
            return modelMapper.map(document, FinanceDocsModel.class);
        } else {
            throw new EntityNotFoundException("Document not found, id= " + id);
        }
    }


}

