package com.example.service;

import com.example.controller.UserController;
import com.example.model.Address;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;
   // @InjectMocks
    UserService userService;

    @Before
    public void SetUp() throws Exception {
         userService = new UserService(userRepository);
        //    initMocks(this);// this is needed for inititalization of mocks, if you use @Mock
        //  Object urlChecker;
        //  UserController controller = new UserController(userService,urlChecker);
        //  MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();
        //  }
    }

    @Test
    public void testSaveUser() throws IOException {

        String userData = ("{\n" +
                "\t\"firstName\": \"testFirstName\",\n" +
                "\t\"lastName\": \"testLastName\",\n" +
                "\t\"email\": \"testEmail@gmail.com\",\n" +
                "\t\"street\": \"testStreet\",\n" +
                "\t\"city\": \"testCity\",\n" +
                "\t\"pinCode\": \"321324\"\n" +
                "}");


        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode  = mapper.readTree(userData);

        User user = new User();
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setEmail("testEmail@gmail.com");
        Address address1 = new Address();
        address1.setStreet("testStreet");
        address1.setCity("testCity");
        address1.setPinCode("321324");
        user.setAddress(address1);


        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        User retrievedUser = userService.save(jsonNode); //Stored Values in retrievedUser

        assertNotNull(retrievedUser);
        assertEquals(0, retrievedUser.getId());
        assertEquals("testFirstName", retrievedUser.getFirstName());
        assertEquals("testLastName", retrievedUser.getLastName());
        assertEquals("testEmail@gmail.com", retrievedUser.getEmail());

        Address retrievedAddress =  retrievedUser.getAddress();//values taken from retrievedUser in assigns to retrievedAddress
        assertEquals("testStreet", retrievedAddress.getStreet());
        assertEquals("testCity", retrievedAddress.getCity());
        assertEquals("321324", retrievedAddress.getPinCode());

    }

    @Test
    public void happyPathTest(){

        User user = new User();
        user.setId(45);

        Mockito.when(userRepository.findById(45)).thenReturn(Optional.of(user));

        ResponseEntity<User> foundById = userService.findById(45);
        assertEquals(HttpStatus.OK, foundById.getStatusCode());
        assertNotNull(foundById.getBody());

        User recievedUser = foundById.getBody();
        assertEquals(45, recievedUser.getId());
    }

    @Test
    public void errorPathTest(){

        Mockito.when(userRepository.findById(45)).thenReturn(Optional.empty());

        ResponseEntity<User> foundById = userService.findById(45);
        assertEquals(HttpStatus.NOT_FOUND, foundById.getStatusCode());
    }


    @Test
    public void deleteUserByIdHappyPathTest() {
        User user = new User();
        user.setId(3);
        Mockito.when(userRepository.findById(3)).thenReturn(Optional.of(user));
        userService.deleteUserById(3);
    }
}