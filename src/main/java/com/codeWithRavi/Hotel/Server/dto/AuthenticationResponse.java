package com.codeWithRavi.Hotel.Server.dto;

import com.codeWithRavi.Hotel.Server.enumms.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwt;
    private Long userId;
    private UserRole userRole;
}
