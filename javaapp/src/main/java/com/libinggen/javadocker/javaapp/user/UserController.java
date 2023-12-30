package com.libinggen.javadocker.javaapp.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  @GetMapping("/{id}")
  public User getUserById(@PathVariable Long id) {
    return userRepository.findById(id).get();
  }

  @PostMapping
  public User createUser(@RequestBody User user) {
    String hashedPassword = userService.hashPassword(user.getPassword());
    user.setPassword(hashedPassword);
    return userRepository.save(user);
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
    User existingUser = userRepository.findById(user.getId()).get();

    if (existingUser == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    boolean isPasswordCorrect =
        userService.checkPassword(user.getPassword(), existingUser.getPassword());
    if (!isPasswordCorrect) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The password is incorrect");
    }

    return ResponseEntity.ok(Map.of("user", existingUser));

  }
}
