package com.example.api.Service;

import com.example.api.Models.AdminModel;
import com.example.api.Models.LoginRequestDto;
import com.example.api.Models.EtudiantModel;
import com.example.api.Models.ReferentAcademiqueModel;
import com.example.api.Repository.AdminRepository;
import com.example.api.Repository.EtudiantRepository;
import com.example.api.Repository.ReferentAcademiqueRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Yassine Deriouch
 * @project API
 */
@Data
@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ReferentAcademiqueRepository referentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(LoginRequestDto authenticationRequest) throws UsernameNotFoundException{
        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        AdminModel admin = adminRepository.findByEmail(email);
        EtudiantModel etudiant = etudiantRepository.findByEmail(email);
        ReferentAcademiqueModel referent = referentRepository.findByEmail(email);

        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            return "admin";
        }
        else if (etudiant != null && passwordEncoder.matches(password, etudiant.getPassword())) {
            return etudiant.getFkrole().getLibelle();
        }
        else if(referent != null && passwordEncoder.matches(password, referent.getPassword())){
            return "referent";
        }
        else {
            throw new UsernameNotFoundException("Bad credentials");
        }
    }
}
