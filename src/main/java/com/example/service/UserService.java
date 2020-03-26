package com.example.service;

import com.example.model.Address;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(JsonNode requestBody) {
        User user = new User();

        user.setFirstName( requestBody.get("firstName").asText() );
        user.setLastName( requestBody.get("lastName").asText());
        user.setEmail( requestBody.get("email").asText() );

        Address address = new Address();
        address.setStreet( requestBody.get("street").asText() );
        address.setCity( requestBody.get("city").asText() );
        address.setPinCode( requestBody.get("pinCode").asText() );
        address.setUser(user);
        user.setAddress(address);
        return userRepository.save(user);
    }

    public ResponseEntity<User> findById(Integer id) {
        Optional<User> userById = userRepository.findById(id);

        if(userById.isPresent()){
            return new ResponseEntity<>(userById.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public void deleteUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            userRepository.deleteById(id);
        }
    }
}