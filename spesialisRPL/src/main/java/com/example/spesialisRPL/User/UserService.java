package com.example.spesialisRPL.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean register(UserData user) {
        try {
            user.setKata_sandi(passwordEncoder.encode(user.getKata_sandi()));
            userRepository.saveUser(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error saat menyimpan user: " + e.getMessage());
            return false;
        }
    }

    public UserData login(String email, String password) {
        
        if(userRepository.findByEmail(email).isPresent()){
            UserData user = userRepository.findByEmail(email).get();

            if(passwordEncoder.matches(password, user.getKata_sandi())){
                return user;
            }
        }
        return null;
    }
}
