package com.libinggen.javadocker.javaapp.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public String hashPassword(String plainPassword) {
        return bCryptPasswordEncoder.encode(plainPassword);
    }

    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return bCryptPasswordEncoder.matches(plainPassword, hashedPassword);
    }

    public boolean isUsernameExists(String username) {
        if (username != null && userRepository.findByUserName(username).isPresent()) {
            return true;
        }
        return false;
    }

    public boolean isEmailExists(String email) {
        if (email != null && userRepository.findByEmail(email).isPresent()) {
            return true;
        }
        return false;
    }
}
