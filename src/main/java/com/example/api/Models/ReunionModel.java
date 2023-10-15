package com.example.api.Models;

import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.sql.Date;
import java.util.Set;

/**
 * Created By Youssef on 16/05/2023
 *
 * @Author : Youssef
 * @Date : 16/05/2023
 * @Project : Gestion-club-uir
 */

@Entity
@Table(name = "ReunionModel")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReunionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_reunion;

    @Column(name = "nom_reunion")
    private String nom_reunion;

    @Column(name = "date_reunion")
    private Date date_reunion;

    @Column(name = "lieu_reunion")
    private String lieu_reunion;

    @Column(name = "link_reunion")
    private String Link_reunion;

    public ReunionModel(String nom_reunion) {
        this.nom_reunion = nom_reunion;
    }

    /////////////////
    @ToStringExclude
    @ManyToMany
    @JoinTable(name = "club_reunion_association",
            joinColumns = @JoinColumn(name = "id_reunion"),
            inverseJoinColumns = @JoinColumn(name = "id_club", referencedColumnName = "id_club"))
    private List<ClubModel> ListClubs;

   /* @ToStringExclude
    @ManyToMany
    @JoinTable(name = "reunion_etudiant_associations",
            joinColumns = @JoinColumn(name = "id_reunion"),
            inverseJoinColumns = @JoinColumn(name = "id_etudiant"))
    private List<EtudiantModel> ListEtudiants;*/


}
