package com.e.ecommerce.service;

import com.e.ecommerce.dto.UserDTO;
import com.e.ecommerce.entity.User;
import com.e.ecommerce.repository.UserRepo;
import com.e.ecommerce.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuth {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public UserDTO register(UserDTO Auth){
        UserDTO authRes = new UserDTO();
        try {
            User user = new User();
            user.setUsername(Auth.getUsername());
            user.setPassword(passwordEncoder.encode(Auth.getPassword()));
            user.setEmail(Auth.getEmail());
            user.setRole(Auth.getRole());
            User ourUserResult = userRepo.save(user);
            if (ourUserResult != null && ourUserResult.getId()>0) {
                authRes.setUser(ourUserResult);
                authRes.setMessage("User Saved Successfully");
                authRes.setStatusCode(200);
            }
        }catch (Exception e){
            authRes.setStatusCode(500);
            authRes.setError(e.getMessage());
        }
        return authRes;
    }

    public UserDTO login(UserDTO Auth){
        UserDTO authRes = new UserDTO();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(Auth.getUsername(),Auth.getPassword()));
            var user = userRepo.findByUsername(Auth.getUsername()).orElseThrow();
            System.out.print("User is "+ user);
            var jwt = jwtUtil.generateToken(user);
            authRes.setStatusCode(200);
            authRes.setToken(jwt);
            authRes.setExpirationTime("24Hr");
            authRes.setMessage("Successfully Signed In");
        }catch (Exception e){
            authRes.setStatusCode(500);
            authRes.setError(e.getMessage());
        }
        return authRes;
    }
}
