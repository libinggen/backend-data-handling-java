package com.libinggen.javadocker.javaapp.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import com.libinggen.javadocker.javaapp.validator.PasswordValidator;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserService userService;

  @GetMapping
  public ResponseEntity<?> getAllUsers() {
    List<User> users = userRepository.findAll();

    List<UserDTO> userDTOs =
        users.stream().map(user -> new UserDTO(user.getUuid(), user.getUserName(), user.getEmail()))
            .collect(Collectors.toList());

    return ResponseEntity.ok(Map.of("data", userDTOs));
  }

  @GetMapping("/user/{uuid}")
  public ResponseEntity<?> getUserByUuid(@PathVariable UUID uuid) {
    Optional<User> userOptional = userRepository.findByUuid(uuid);

    if (!userOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    User existingUser = userOptional.get();
    UserDTO userDTO =
        new UserDTO(existingUser.getUuid(), existingUser.getUserName(), existingUser.getEmail());

    return ResponseEntity.ok(Map.of("data", userDTO));
  }

  @PostMapping("/create-user")
  public ResponseEntity<?> createUser(@RequestBody User user) {
    try {
      PasswordValidator.validatePasswordComplexity(user.getPassword());

      String hashedPassword = userService.hashPassword(user.getPassword());
      user.setPassword(hashedPassword);
      User createUser = userRepository.save(user);
      UserDTO userDTO =
          new UserDTO(createUser.getUuid(), createUser.getUserName(), createUser.getEmail());

      return ResponseEntity.ok(Map.of("data", userDTO));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/update-user/{uuid}")
  public ResponseEntity<?> updateUser(@PathVariable UUID uuid, @RequestBody User user) {
    try {
      Optional<User> userOptional = userRepository.findByUuid(uuid);

      if (!userOptional.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
      }

      PasswordValidator.validatePasswordComplexity(user.getPassword());

      User existingUser = userOptional.get();
      boolean isPasswordCorrect =
          userService.checkPassword(user.getPassword(), existingUser.getPassword());
      if (!isPasswordCorrect) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The password is incorrect");
      }

      existingUser.setUserName(user.getUserName());
      existingUser.setEmail(user.getEmail());
      String hashedPassword = userService.hashPassword(user.getPassword());
      existingUser.setPassword(hashedPassword);
      UserDTO userDTO =
          new UserDTO(existingUser.getUuid(), existingUser.getUserName(), existingUser.getEmail());

      return ResponseEntity.ok(Map.of("data", userDTO));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/delete-user/{uuid}")
  public ResponseEntity<?> deleteUser(@PathVariable UUID uuid) {
    try {
      userRepository.findByUuid(uuid).get();
      userRepository.deleteByUuid(uuid);
      return ResponseEntity.ok(Map.of("delete", "User deleted successfully"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody User user) {
    try {
      Optional<User> userOptional = userRepository.findByUserName(user.getUserName());

      if (!userOptional.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
      }

      User existingUser = userOptional.get();

      PasswordValidator.validatePasswordComplexity(user.getPassword());

      if (existingUser == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
      }

      boolean isPasswordCorrect =
          userService.checkPassword(user.getPassword(), existingUser.getPassword());
      if (!isPasswordCorrect) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The password is incorrect");
      }

      UserDTO userDTO =
          new UserDTO(existingUser.getUuid(), existingUser.getUserName(), existingUser.getEmail());

      return ResponseEntity.ok(Map.of("data", userDTO));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
