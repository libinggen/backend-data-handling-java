package com.libinggen.javadocker.javaapp.user;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private UUID uuid;
    private String userName;
    private String email;
}
