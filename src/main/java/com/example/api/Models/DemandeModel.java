package com.example.api.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "demandeModel")
public class DemandeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDemande;
    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private EtudiantModel etudiant;
    @ManyToOne
    @JoinColumn(name = "id_club")

    private ClubModel club;    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private DemandeStatut statut;
}
