package com.example.repository;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    public void myFirstSpringBootTest() {
        User user = new User();
        user.setFirstName("TestFirstName1");
        user.setLastName("TestLastName1");
        user.setEmail("TestEmail1");

        testEntityManager.persist(user);
        testEntityManager.flush();

        Optional<User> byId = userRepository.findById(1);

        assertTrue( byId.isPresent() );
    }

}
