package com.example.api.Repository;

import com.example.api.Models.ClubModel;
import com.example.api.Models.DocumentModel;
import com.example.api.Models.DocumentsAdminResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import javax.print.Doc;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Yassine
 * @project API
 */
@Repository
public interface DocumentRepository extends JpaRepository<DocumentModel, Integer> {
    DocumentModel findDocumentByFileName(String fileName);
    List<DocumentModel> findDocumentByDateEnvoi(Date dateEnvoi);
    List<DocumentModel> findDocumentModelByReferent_ClubModelList(ClubModel club);

}
