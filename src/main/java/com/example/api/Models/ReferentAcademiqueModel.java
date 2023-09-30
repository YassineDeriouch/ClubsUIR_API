package com.example.api.Models;

import jakarta.persistence.*;
import lombok.Data;

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
    @Column(name="email")
    private String email;
    @Column(name="password")
    private String password;

    // Relation with ClubModel (1,n) : 1 referent academique can have many clubs
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "referent_club_association",
            joinColumns = @JoinColumn(name = "id_referent"),
            inverseJoinColumns = @JoinColumn(name = "id_club"))
    private List<ClubModel> clubModelList;
}
