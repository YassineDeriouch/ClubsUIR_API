    package com.example.api.Models;
    import jakarta.persistence.*;
    import lombok.Data;

    import java.sql.Date;
    import java.util.List;

    @Entity
    @Table(name="evenementModel")
    @Data
    public class EvenementModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id_evenement;
        @Column(name="libelle")
        private String libelle;
        @Column(name="date_debut")
        private Date date_debut;
        @Column(name="date_fin")
        private Date date_fin;
        @Column(name="Type_event")
        private String type;
        @Column(name="budget")
        private double budget;
        @Column(name="statut_event")
        @Enumerated(EnumType.STRING)
        private EvenementStatut statut;

        // Relation with EtudiantModel (1,n) : 1 evenement can have many participants
        @OneToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "evenement_Club_association",
                joinColumns = @JoinColumn(name = "id_evenement"),
                inverseJoinColumns = @JoinColumn(name = "id_club"))
        private List<ClubModel> participants;

    }
