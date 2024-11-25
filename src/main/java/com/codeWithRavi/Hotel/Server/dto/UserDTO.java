package com.codeWithRavi.Hotel.Server.dto;

import com.codeWithRavi.Hotel.Server.enumms.UserRole;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private UserRole userRole;
}
