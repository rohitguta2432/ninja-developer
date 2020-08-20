package io.rammila.api.controller;

import io.rammila.api.model.User;
import io.rammila.api.repository.UserRepository;
import io.rammila.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public ResponseEntity<?> save(@RequestBody User user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }

    @GetMapping("{userId}")
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public ResponseEntity<?> getById(@PathVariable UUID userId) {
        return new ResponseEntity<>(userRepository.findById(userId), HttpStatus.OK);
    }
}
