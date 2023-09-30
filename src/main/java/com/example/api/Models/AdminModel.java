    package com.example.api.Models;

    import jakarta.persistence.*;
    import lombok.Data;

    /**
     * @author Yassine Deriouch
     * @project API
     */

    @Data
    @Entity
    @Table(name = "adminModel")
    public class AdminModel {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_admin")
        private int id_admin;
        @Column(name="nom")
        private String nom;
        @Column(name="prenom")
        private String prenom;
        @Column(name="email")
        private String email;
        @Column(name="password")
        private String password;
        @Column(name="telephone")
        private String telephone;
    }
