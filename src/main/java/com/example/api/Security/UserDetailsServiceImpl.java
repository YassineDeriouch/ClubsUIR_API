package com.example.api.Security;

import com.example.api.Models.EtudiantModel;
import com.example.api.Repository.EtudiantRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yassine Deriouch
 * @project API
 */
@Service
@Data
public class UserDetailsServiceImpl implements UserDetailsService {
   @Autowired
   private EtudiantRepository etudiantRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EtudiantModel etudiant = etudiantRepository.findByEmail(username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(etudiant.getFkrole().getLibelle()));
        System.out.println("======================>>>>>"+etudiant.getFkrole().getLibelle());
        return new User(etudiant.getEmail(), etudiant.getPassword(), authorities);
    }
}