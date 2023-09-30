package com.example.api.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Yassine Deriouch
 * @project API
 */
@Data
@AllArgsConstructor
public class LogoutResponseDto {
    LoginRequestDto user;
    private String cookieUuid;
    private String message;


}
