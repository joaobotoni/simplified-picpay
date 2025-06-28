package com.simplified.picpay.controller;

import com.simplified.picpay.model.domain.user.User;
import com.simplified.picpay.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        try {
            List<User> user = service.getAll();
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            ResponseEntity.badRequest().build();
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = service.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            ResponseEntity.badRequest().build();
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody User user) {
        try {
            service.create(user);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            ResponseEntity.badRequest().build();
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            ResponseEntity.badRequest().build();
            throw new RuntimeException(e);
        }
    }
}
