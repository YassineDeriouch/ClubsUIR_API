package com.example.api.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

/**
 * @author Yassine Deriouch
 * @project ClubsUIR
 */
@Data
public class DocumentsAdminResponseDTO {
    private int idDocument;
    private String libelle;
    private Date dateEnvoi;
    private String nom;
    private String prenom;
}
