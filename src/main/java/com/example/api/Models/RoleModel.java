package com.example.api.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data // replace getters,setters ...
@Entity
@Table(name="roleModel")
public class RoleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int idRole;

    @Column(name="libelle", unique = true)
    private String libelle;

}
