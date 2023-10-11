package com.example.api.Service;

import com.example.api.Models.AdminModel;
import com.example.api.Models.CompteDto;
import com.example.api.Models.EtudiantModel;
import com.example.api.Models.ReferentAcademiqueModel;
import com.example.api.Repository.AdminRepository;
import com.example.api.Repository.EtudiantRepository;
import com.example.api.Repository.ReferentAcademiqueRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private EtudiantService etudiantService;
    @Autowired
    private ReferentAcademiqueService referentAcademiqueService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ReferentAcademiqueRepository referentRepository;

    public CompteDto login(String login, String password) {
        CompteDto compteDto = new CompteDto();

        if (etudiantService.findEtudiantModelByEmail(login) && etudiantService.findEtudiantModelByPassword(password)) {
            EtudiantModel e = etudiantRepository.findByEmail(login);
            compteDto.setIcompte(e.getId_etudiant());
            compteDto.setLogin(login);
            compteDto.setPassword(password);
            compteDto.setRole(e.getFkrole().getLibelle());
        } else if (adminService.findAdminModelByEmail(login) && adminService.findAdminModelByPassword(password)) {
            AdminModel adminModel = adminRepository.findByEmail(login);
            compteDto.setIcompte(adminModel.getId_admin());
            compteDto.setLogin(login);
            compteDto.setPassword(password);
            compteDto.setRole("admin");
        } else if (referentAcademiqueService.findReferentAcademiqueModelByEmail(login) && referentAcademiqueService.findReferentAcademiqueModelByPassword(password)) {
            ReferentAcademiqueModel referentAcademiqueModel = referentRepository.findByEmail(login);
            compteDto.setIcompte(referentAcademiqueModel.getId_referent());
            compteDto.setLogin(login);
            compteDto.setPassword(password);
            compteDto.setRole("referent");
        } else {
            throw new RuntimeException("Authentication failed: Invalid email or password");
        }

        return compteDto;
    }

}



