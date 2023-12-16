package com.example.api.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.graphql.ConditionalOnGraphQlSchema;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

/**
 * @author Yassine
 * @project API
 */
@Entity
@Table(name = "Finance_model")
@Data
public class FinanceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id_financement;

    @JsonIgnore
    @Column(name = "budget")
    private double budget;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "depenses")
    private double depenses;

    @Column(name = "annee")
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy")
    private Date annee;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_evenement")
    private EvenementModel eventModel;

    @OneToOne
    @JoinColumn(name = "id_club")
    private ClubModel clubModel;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_etudiant")
    private EtudiantModel etudiant;

}
