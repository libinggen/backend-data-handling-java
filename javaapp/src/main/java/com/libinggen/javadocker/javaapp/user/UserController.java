package com.libinggen.javadocker.javaapp.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserService userService;

  @GetMapping
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @GetMapping("/{uuid}")
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

  @PostMapping
  public ResponseEntity<?> createUser(@RequestBody User user) {
    String hashedPassword = userService.hashPassword(user.getPassword());
    user.setPassword(hashedPassword);
    User createUser = userRepository.save(user);
    UserDTO userDTO =
        new UserDTO(createUser.getUuid(), createUser.getUserName(), createUser.getEmail());

    return ResponseEntity.ok(Map.of("data", userDTO));
  }

  @PutMapping("/{id}")
  public User updateUser(@PathVariable Long id, @RequestBody User user) {
    User existingUser = userRepository.findById(id).get();
    existingUser.setUserName(user.getUserName());
    existingUser.setEmail(user.getEmail());
    return userRepository.save(existingUser);
  }

  @DeleteMapping("/{id}")
  public String deleteUser(@PathVariable Long id) {
    try {
      userRepository.findById(id).get();
      userRepository.deleteById(id);
      return "User deleted successfully";
    } catch (Exception e) {
      return "User not found";
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody User user) {
    Optional<User> userOptional = userRepository.findByUserName(user.getUserName());

    if (!userOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    User existingUser = userOptional.get();

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
  }
}
