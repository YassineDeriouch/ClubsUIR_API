package com.example.api.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "reviewevent_model")
@Data
public class ReviewEventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReview;

    private String Description;

    private String Nom;

    @Enumerated(EnumType.STRING)
    private ReviewEvent_Post_Pre Type;

}
