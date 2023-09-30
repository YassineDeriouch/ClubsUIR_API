package com.example.api.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data // replace getters,setters ...
@Entity
@Table(name="etudiantModel")
public class EtudiantModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etudiant")
    private int id_etudiant;
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

    //Relation Many to one(Many etudiant have one role)
    @ManyToOne
    @JoinColumn(name="idRole")
    private RoleModel fkrole;

    //Relation Many to many(Many etudiant have many club)
    @ManyToMany
    @JoinTable(name = "club_etudiant_associations",
            joinColumns = @JoinColumn(name = "id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "id_club"))
    private List<ClubModel> clubModelList;

    //Relation Many to many(Many etudiant have many reunion)
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "reunion_etudiant_associations",
            joinColumns = @JoinColumn(name = "id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "id_reunion"))
    private List<ReunionModel> reunionModelList;

}