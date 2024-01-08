package com.libinggen.javadocker.javaapp.user;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
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
