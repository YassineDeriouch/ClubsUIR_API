package com.example.api.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

/**
 * @author Yassine Deriouch
 * @project API
 */

@Data
@Entity
@Table
public class FinanceDocsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_document;

    @Column(name = "libelle")
    private String libelle;

    @JsonIgnore
    @Column(name = "file", columnDefinition = "MEDIUMBLOB")
    private byte[] file;

    @Column(name = "dateEnvoi")
    @CreatedDate
    private java.sql.Date dateEnvoi;

    @JsonIgnore  private String fileName, fileType;

}
