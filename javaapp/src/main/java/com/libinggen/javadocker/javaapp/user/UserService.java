package com.libinggen.javadocker.javaapp.user;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

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
}
