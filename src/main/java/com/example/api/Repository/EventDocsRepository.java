package com.example.api.Repository;

import com.example.api.Models.ClubModel;
import com.example.api.Models.DocumentModel;
import com.example.api.Models.EventDocsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Youssef
 * @project API
 */
@Repository
public interface EventDocsRepository extends JpaRepository<EventDocsModel, Integer> {
    EventDocsModel findDocumentByFileName(String fileName);
    List<EventDocsModel> findDocumentByDateEnvoi(Date dateEnvoi);
    EventDocsModel findDocumentModelByLibelle(String libelle);
}
