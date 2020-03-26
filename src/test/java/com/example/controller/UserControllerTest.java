package com.example.controller;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class UserControllerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;


    @Test
    public void errorPathNoBodyWhilePosting() throws Exception {

        mvc.perform(post("/users"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void happyPathValidPost() throws Exception {

        mvc.perform(post("/users")
                .content("{\n" +
                "\t\"firstName\": \"testFirstName\",\n" +
                "\t\"lastName\": \"testLastName\",\n" +
                "\t\"email\": \"testEmail@gmail.com\",\n" +
                "\t\"street\": \"testStreet\",\n" +
                "\t\"city\": \"testCity\",\n" +
                "\t\"pinCode\": \"321324\"\n" +
                "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void errorPathGetWithoutPost() throws Exception {
        Mockito.when(userService.findById(100)).thenReturn(new ResponseEntity<>( HttpStatus.NOT_FOUND));

        mvc.perform(get("/users/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void happyPathValidPostAndGet() throws Exception {

        User dummyResponse = new User();
        dummyResponse.setId(1);
        Mockito.when(userService.findById(1)).thenReturn(new ResponseEntity<>(dummyResponse, HttpStatus.OK));

        mvc.perform(get("/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void deleteApplication() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .delete("/users/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}