    package com.example.api.Models;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.Data;

    import java.util.List;

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
        @Column(name="emailBDE")
        private String emailBDE;
        @Column(name="password")
        private String password;
        @Column(name="telephone")
        private String telephone;

        @JsonIgnore
        @JsonBackReference
        @OneToMany(mappedBy = "adminModel")
        public List<DocumentModel> adminDocuments;

        @JsonIgnore
        String adminProfilePicturePath = new ImageModel().getFilePath();

        @JsonIgnore
        String adminProfilePictureName = new ImageModel().getFileName();
    }
