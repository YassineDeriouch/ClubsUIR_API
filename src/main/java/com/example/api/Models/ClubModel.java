    package com.example.api.Models;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.ToString;
    import org.apache.commons.lang3.builder.ToStringExclude;

    import java.util.List;


    @Entity
    @Data // getters,setters ...
    @Table(name="clubModel")
    public class ClubModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id_club;

        @Column(name="libelle", nullable = false)
        private String libelle;

        @Enumerated(EnumType.STRING)
        @Column(name="type", nullable = false)
        private ClubTypes type;

        //Enumerated .STRING est utilisé pour stocker la valeur de l'énumération sous forme de chaîne
        @Enumerated(EnumType.STRING)
        @Column(name = "statut")
        private ClubStatut statut;

        @ToStringExclude
        @JsonIgnore
        @ManyToOne (optional = true)
        private ReferentAcademiqueModel referent;

        @ToStringExclude
        @JsonIgnore
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "club_etudiant_associations",
                joinColumns = @JoinColumn(name = "id_club"),
                inverseJoinColumns = @JoinColumn(name = "id_etudiant"))
        private List<EtudiantModel> etudiantModelList;

        @ToStringExclude
        @JsonIgnore
        @OneToMany
        @JoinTable(name = "document_club",
                joinColumns = @JoinColumn(name = "club_id"),
                inverseJoinColumns = @JoinColumn(name = "document_id"))
        private List<DocumentModel> documentModelList;

        @ToStringExclude
        @JsonIgnore
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "club_reunion_association",
                joinColumns = @JoinColumn(name = "id_club"),
                inverseJoinColumns = @JoinColumn(name = "id_reunion", referencedColumnName = "id_reunion"))
        private List<ReunionModel> reunionModel;



    }
