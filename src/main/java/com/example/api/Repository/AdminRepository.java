package com.example.api.Repository;

import com.example.api.Models.AdminModel;
import com.sun.jdi.IntegerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yassine Deriouch
 * @project API
 */
@Repository
public interface AdminRepository extends JpaRepository<AdminModel , Integer>{
    AdminModel findByEmail(String email);
    AdminModel findByPassword(String password);
}
