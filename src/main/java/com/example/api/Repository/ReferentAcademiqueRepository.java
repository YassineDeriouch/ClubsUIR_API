package com.example.api.Repository;

import com.example.api.Models.ReferentAcademiqueModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yassine
 * @project API
 */
@Repository
public interface ReferentAcademiqueRepository extends JpaRepository<ReferentAcademiqueModel, Integer> {

    ReferentAcademiqueModel findByEmail(String email);
}
