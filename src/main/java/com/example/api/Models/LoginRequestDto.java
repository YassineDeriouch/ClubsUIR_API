package com.example.api.Models;

import lombok.Data;

/**
 * @author Yassine Deriouch
 * @project API
 */
@Data
public class LoginRequestDto {
    /**
     * encapsulate credentials (email, password) as one object To avoid exposing them in URL like this :
     * http://localhost:8081/authentication/login?email=admin&password=admin
     */
    private String email;
    private String password;
}
