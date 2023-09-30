package com.example.api.Repository;

import com.example.api.Models.FinanceDocsModel;
import com.example.api.Models.FinanceDocsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Yassine Deriouch
 * @project API
 */
@Repository
public interface FinanceDocsRepository extends JpaRepository<FinanceDocsModel, Integer> {
    FinanceDocsModel findDocumentByFileName(String fileName);
    List<FinanceDocsModel> findDocumentByDateEnvoi(Date dateEnvoi);
    FinanceDocsModel findDocumentModelByLibelle(String libelle);
}
