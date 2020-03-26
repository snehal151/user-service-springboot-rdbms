package com.example.controller;

import com.example.model.Address;
import com.example.model.User;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User saveUser(@RequestBody JsonNode requestBody) {

        return userService.save(requestBody);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {

        return userService.findById(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id){
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}
