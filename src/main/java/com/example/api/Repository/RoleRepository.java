package com.example.api.Repository;

import com.example.api.Models.EtudiantModel;
import com.example.api.Models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Youssef
 * @project Gestion-club-uir
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Integer> {
    // here add custom jpa methods

    //Method to get role by libelle
    //@Query("SELECT r FROM RoleModel r WHERE r.libelle = :libelle")
    //RoleModel findByLibelle(@Param("libelle") String libelle);
    RoleModel findRoleModelByLibelle(String libelle);
}
