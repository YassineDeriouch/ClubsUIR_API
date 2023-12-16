package com.example.api.Controller;

import com.example.api.Models.CompteDto;
import com.example.api.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Data
@RequiredArgsConstructor
@RestController
@RequestMapping("authentication")
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "Login system")
public class AuthController {

    private final AuthService authService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Operation(summary= "login",description = "sign in an existing user in the DB")
    @PostMapping("/login")
    public ResponseEntity<CompteDto> Authentication(@RequestParam String email,@RequestParam String password){
        try{
            CompteDto compteDto = authService.login(email,password);
            return ResponseEntity.ok(compteDto);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*@PostMapping("/login")
    public ResponseEntity<Map<String, String>> userAuthentication(@RequestBody LoginRequestDto loginRequestDto){
        try{
            String auth = authService.login(loginRequestDto);
            HttpSession session = request.getSession();
            System.out.println("session ========> "+ session.toString());
            Map<String, String> response = new HashMap<>();
            response.put("role", auth);

            return ResponseEntity.ok(response);
            //return new ResponseEntity<>(auth, HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

   /* @PostMapping("/logout")
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
    }*/



   /* @GetMapping("/test")
    @PreAuthorize("hasAuthority('etd')")
    public String etudiantEndpoint() {
        return "Test message for etudiant";
    }*/





}



