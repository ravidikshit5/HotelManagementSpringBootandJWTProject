package com.codeWithRavi.Hotel.Server.controller.auth;

import com.codeWithRavi.Hotel.Server.dto.AuthenticationRequest;
import com.codeWithRavi.Hotel.Server.dto.AuthenticationResponse;
import com.codeWithRavi.Hotel.Server.dto.SignupRequest;
import com.codeWithRavi.Hotel.Server.dto.UserDTO;
import com.codeWithRavi.Hotel.Server.entity.User;
import com.codeWithRavi.Hotel.Server.repository.UserRepository;
import com.codeWithRavi.Hotel.Server.services.auth.AuthService;
import com.codeWithRavi.Hotel.Server.services.jwt.UserService;
import com.codeWithRavi.Hotel.Server.utill.JwtUtil;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    //@Autowired
    private final  AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
        try {
            UserDTO createdUser = authService.createUser(signupRequest);
            return new ResponseEntity<>(createdUser, HttpStatus.OK);
        }catch (EntityExistsException entityExistsException){
            return new ResponseEntity<>("user already exists",HttpStatus.NOT_ACCEPTABLE);
        }catch (Exception e){
            return new ResponseEntity<>("user not created, come again later",HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect username or password");
        }
        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if(optionalUser.isPresent()){
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;

    }

}
