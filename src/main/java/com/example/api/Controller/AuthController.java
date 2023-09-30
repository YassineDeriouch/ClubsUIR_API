package com.example.api.Controller;

import com.example.api.Models.LoginRequestDto;
import com.example.api.Service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Data
@RestController
@RequestMapping("authentication")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private HttpServletRequest request;
    @Autowired private HttpServletResponse response;

    @PostMapping("/login") //pass object instead of params
    public ResponseEntity<Map<String, String>> userAuthentication(@RequestBody LoginRequestDto loginRequestDto){
        try{
            String auth = authService.login(loginRequestDto);
            HttpSession session = request.getSession();
            System.out.println("session ========> "+ session.toString());
            Map<String, String> response = new HashMap<>();
            response.put("role", auth);

            return ResponseEntity.ok(response);
            //return new ResponseEntity<>(auth, HttpStatus.OK);
        }catch (UsernameNotFoundException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Retrieve the current session
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Invalidate the session
            session.invalidate();
            // Clear the session cookie on the client-side
            Cookie sessionCookie = new Cookie("JSESSIONID", null);
            sessionCookie.setMaxAge(0);
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);
            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No session found, or user already logged out");
        }
    }



   /* @GetMapping("/test")
    @PreAuthorize("hasAuthority('etd')")
    public String etudiantEndpoint() {
        return "Test message for etudiant";
    }*/



}

