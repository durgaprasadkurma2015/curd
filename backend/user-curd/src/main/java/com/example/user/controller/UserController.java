package com.example.user.controller;



import com.example.user.model.User;
import com.example.user.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
            .map(user -> ResponseEntity.ok(user))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // collect errors
            StringBuilder sb = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> {
                sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
            });
            return ResponseEntity.badRequest().body(sb.toString());
        }
        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @Valid @RequestBody User userDetails,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> {
                sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
            });
            return ResponseEntity.badRequest().body(sb.toString());
        }
        return userRepository.findById(id).map(user -> {
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setAge(userDetails.getAge());
            user.setDateOfBirth(userDetails.getDateOfBirth());
            user.setMobileNumber(userDetails.getMobileNumber());
            user.setEmailId(userDetails.getEmailId());
            user.setCity(userDetails.getCity());
            user.setDistrict(userDetails.getDistrict());
            user.setState(userDetails.getState());
            user.setCountry(userDetails.getCountry());
            user.setAddress(userDetails.getAddress());
            User updated = userRepository.save(user);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}

