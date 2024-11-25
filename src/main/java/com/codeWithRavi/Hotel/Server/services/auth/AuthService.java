package com.codeWithRavi.Hotel.Server.services.auth;

import com.codeWithRavi.Hotel.Server.dto.SignupRequest;
import com.codeWithRavi.Hotel.Server.dto.UserDTO;

public interface AuthService  {
    UserDTO createUser(SignupRequest signupRequest);
}
