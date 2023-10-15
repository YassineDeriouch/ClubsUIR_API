package com.example.api.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Yassine
 * @project API
 */
@Entity
@Data
@Table(name = "documentModel")
public class DocumentModel {
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

    @ManyToOne(optional = true)
    private ReferentAcademiqueModel referent;

    @ManyToOne(optional = true)
    @JoinColumn(name = "admin_model_id")
    private AdminModel adminModel ;

    @ManyToOne
    @JoinTable(
            name = "document_club",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "club_id"))
    private ClubModel club;




}
