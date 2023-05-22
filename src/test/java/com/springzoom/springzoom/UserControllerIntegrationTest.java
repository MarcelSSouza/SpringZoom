package com.springzoom.springzoom;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.springzoom.springzoom.Controller.UserController;

import com.springzoom.springzoom.Entity.User;
import com.springzoom.springzoom.Repository.UserRepository;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = new User(1L, "John", "john@example.com", "password", new HashSet<>());
        user2 = new User(2L, "Jane", "jane@example.com", "password", new HashSet<>());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> userList = Arrays.asList(user1, user2);
        
        given(userRepository.findAll()).willReturn(userList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$[0].email", is("john@example.com")))
                .andExpect(jsonPath("$[1].name", is("Jane")))
                .andExpect(jsonPath("$[1].email", is("jane@example.com")));
    }

    @Test
    public void testGetUserById() throws Exception {
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }



    // Test for deleteUser
    @Test
    public void testDeleteUser() throws Exception {
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    // Test for getContacts
    @Test
    public void testGetContacts() throws Exception {
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));

        mockMvc.perform(get("/users/1/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // Test for addContact
    @Test
    public void testAddContact() throws Exception {
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        given(userRepository.findById(2L)).willReturn(Optional.of(user2));

        mockMvc.perform(post("/users/1/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":2,\"name\":\"Jane\",\"email\":\"jane@example.com\",\"password\":\"password\",\"contacts\":[]}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Contact added successfully"));
    }

    // Test for removeContact
    @Test
    public void testRemoveContact() throws Exception {
        user1.getContacts().add(user2);
        user2.getContacts().add(user1);

        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        given(userRepository.findById(2L)).willReturn(Optional.of(user2));

        mockMvc.perform(delete("/users/1/contacts/2"))
                .andExpect(status().isOk());
    }
}
