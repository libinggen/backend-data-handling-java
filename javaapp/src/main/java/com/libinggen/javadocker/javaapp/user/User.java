package com.libinggen.javadocker.javaapp.user;

import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(name = "username", unique = true, length = 255)
    private String userName;

    @Email(message = "Enter a valid email address.")
    @Column(name = "email", unique = true, length = 250)
    private String email;

    @Length(max = 128)
    @Column(name = "password", length = 128)
    private String password;

    @Length(max = 128)
    @Column(name = "password2", length = 128)
    private String password2;

    @PrePersist
    public void initializeUUID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
