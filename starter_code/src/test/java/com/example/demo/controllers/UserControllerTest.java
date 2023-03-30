package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.junit.Assert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {

    UserRepository userRepository=mock(UserRepository.class);

    CartRepository cartRepository=mock(CartRepository.class);

    UserController userController;

    BCryptPasswordEncoder encoder =mock(BCryptPasswordEncoder.class);



    @Before
    public void setup(){
        userController =new UserController();

        testUtilities.injectObject(userController,"userRepository",userRepository);
        testUtilities.injectObject(userController,"cartRepository",cartRepository);
        testUtilities.injectObject(userController,"bCryptPasswordEncoder",encoder);
    }

    @Test
    public void create_user() throws Exception{
        when(encoder.encode("userPassword")).thenReturn("HashedPassword");
        CreateUserRequest req = new CreateUserRequest();

        req.setUsername("username");
        req.setPassword("userPassword");
        req.setConfirmPassword("userPassword");

         ResponseEntity<User> response= userController.createUser(req);

         Assert.assertNotNull(response);
         Assert.assertEquals(200,response.getStatusCodeValue());

         User user =response.getBody();

         Assert.assertNotNull(user);
         Assert.assertEquals("username",user.getUsername());
         Assert.assertEquals(0,user.getId());
         Assert.assertEquals("HashedPassword",user.getPassword());
    }

    @Test
    public void find_user_by_username_andID() throws Exception{

        when(encoder.encode("userPassword")).thenReturn("HashedPassword");
        CreateUserRequest req = new CreateUserRequest();

        req.setUsername("username");
        req.setPassword("userPassword");
        req.setConfirmPassword("userPassword");

        ResponseEntity<User> response= userController.createUser(req);

        User user =response.getBody();

         Assert.assertNotNull(userController.findByUserName(user.getUsername()));
         Assert.assertEquals("username",user.getUsername());
         Assert.assertNotNull(userController.findById(user.getId()));

    }




}
