package com.example.api.Models;

import lombok.Data;

/**
 * @author Yassine Deriouch
 * @project ClubsUIR
 */

@Data
public class AdminDTO {
    private int id_admin;
    private String nom;
    private String prenom;
    private String email;
    private String emailBDE;
    private String password;
    private String telephone;
}
