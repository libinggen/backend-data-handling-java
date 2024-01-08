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
import com.libinggen.javadocker.javaapp.utility.JwtUtil;
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

      if (userService.isUsernameExists(user.getUserName())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
      }
      if (userService.isEmailExists(user.getEmail())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
      }

      String hashedPassword = userService.hashPassword(user.getPassword());
      user.setPassword(hashedPassword);
      User createUser = userRepository.save(user);
      UserDTO userDTO =
          new UserDTO(createUser.getUuid(), createUser.getUserName(), createUser.getEmail());

      String token = JwtUtil.generateToken(user.getUserName());
      return ResponseEntity.ok(Map.of("token", token, "data", userDTO));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/update-user/{uuid}")
  public ResponseEntity<?> updateUser(@PathVariable UUID uuid, @RequestBody User user) {
    try {
      String password = user.getPassword();
      String password2 = user.getPassword2();
      String userName = user.getUserName();
      String email = user.getEmail();
      if (password == null || password.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The password cannot be empty");
      }
      // Fetch existing user
      Optional<User> userOptional = userRepository.findByUuid(uuid);
      if (!userOptional.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
      }

      User existingUser = userOptional.get();

      // Check if the current password is correct
      PasswordValidator.validatePasswordComplexity(password);
      boolean isPasswordCorrect = userService.checkPassword(password, existingUser.getPassword());
      if (!isPasswordCorrect) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The password is incorrect");
      }

      // Check change
      if ((password2 == null || password2.isEmpty()) && (userName == null || userName.isEmpty())
          && (email == null || email.isEmpty())
          || (password2 != null && (password2.isEmpty() || password2.equals(password)))
              && (userName != null
                  && (userName.isEmpty() || existingUser.getUserName().equals(userName)))
              && (email != null && email.isEmpty() || existingUser.getEmail().equals(email))) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Must include new password or username or email.");
      }

      // Update password if provided
      if (password2 != null && !password2.isEmpty()) {
        PasswordValidator.validatePasswordComplexity(password2);
        if (password.equals(password2)) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("New password cannot be the same as the current password.");
        }
        String hashedNewPassword = userService.hashPassword(password2);
        existingUser.setPassword(hashedNewPassword);
      }

      // Check for username uniqueness
      if (userName != null && !userName.isEmpty()
          && ((email == null || email.isEmpty()) && existingUser.getUserName().equals(userName)
              || !existingUser.getUserName().equals(userName))) {
        boolean userNameExists = userService.isUsernameExists(userName);
        if (userNameExists) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already in use");
        }
        existingUser.setUserName(userName);
      }

      // Check for email uniqueness
      if (email != null && !email.isEmpty()
          && ((userName == null || userName.isEmpty())
              && existingUser.getUserName().equals(userName)
              || !existingUser.getEmail().equals(email))) {
        boolean emailExists = userService.isEmailExists(email);
        if (emailExists) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already in use");
        }
        existingUser.setEmail(email);
      }

      // Save the updated user
      User storedUser = userRepository.save(existingUser);
      UserDTO userDTO =
          new UserDTO(storedUser.getUuid(), storedUser.getUserName(), storedUser.getEmail());

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

      String token = JwtUtil.generateToken(user.getUserName());
      return ResponseEntity.ok(Map.of("token", token, "data", userDTO));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
