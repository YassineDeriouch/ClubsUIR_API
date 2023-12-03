package com.example.api.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.List;

@Data // replace getters,setters ...
@Entity
@Table(name="referentAcademiqueModel")
public class ReferentAcademiqueModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_referent;
    @Column(name="nom")
    private String nom;
    @Column(name="prenom")
    private String prenom;
    @Column(name="email", unique = true)
    private String email;
    @Column(name="password", unique = true)
    private String password;
    @Column(name="telephone")
    private String telephone;

    // Relation with ClubModel (1,n) : 1 referent academique can have many clubs
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "referent_club_association",
            joinColumns = @JoinColumn(name = "id_referent"),
            inverseJoinColumns = @JoinColumn(name = "id_club"))
    private List<ClubModel> clubModelList;

    @JsonIgnore String referentProfilePicturePath = new ImageModel().getFilePath();
    @JsonIgnore String referentProfilePictureName = new ImageModel().getFileName();
}

