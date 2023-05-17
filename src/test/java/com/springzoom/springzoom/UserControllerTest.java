package com.springzoom.springzoom;

import com.springzoom.springzoom.Controller.UserController;
import com.springzoom.springzoom.Entity.User;
import com.springzoom.springzoom.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers_ReturnsAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "User1", "user1@example.com"));
        users.add(new User(2L, "User2", "user2@example.com"));

        when(userRepository.findAll()).thenReturn(users);

        List<User> response = userController.getAllUsers();

        assertEquals(users, response);
    }

    @Test
    void testGetUserById_ValidUserId_ReturnsUser() {
        Long userId = 1L;
        User user = new User(userId, "User1", "user1@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User response = userController.getUserById(userId);

        assertEquals(user, response);
    }

    @Test
    void testGetUserById_InvalidUserId_ThrowsException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userController.getUserById(userId));
    }

    @Test
    void testCreateUser_ValidUser_ReturnsCreatedUser() {
        User user = new User(1L, "User1", "user1@example.com");

        when(userRepository.save(user)).thenReturn(user);

        User response = userController.createUser(user);

        assertEquals(user, response);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testLoginUser_ValidCredentials_ReturnsUser() {
        User user = new User(1L, "User1", "user1@example.com", "password");

        when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(user);

        ResponseEntity<User> response = userController.loginUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testLoginUser_InvalidCredentials_ReturnsNotFound() {
        User user = new User(1L, "User1", "user1@example.com", "password");

        when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(null);

        ResponseEntity<User> response = userController.loginUser(user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateUser_ValidUserId_ReturnsUpdatedUser() {
        Long userId = 1L;
        User existingUser = new User(userId, "User1", "user1@example.com");
        User updatedUser = new User(userId, "UpdatedUser1", "updateduser1@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User response = userController.updateUser(userId, updatedUser);

        assertEquals(existingUser, response);
        assertEquals(updatedUser.getName(), existingUser.getName());
        assertEquals(updatedUser.getEmail(), existingUser.getEmail());
        assertEquals(updatedUser.getPassword(), existingUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_InvalidUserId_ThrowsException() {
        Long userId = 1L;
        User updatedUser = new User(userId, "UpdatedUser1", "updateduser1@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userController.updateUser(userId, updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_ValidUserId_ReturnsOk() {
        Long userId = 1L;
        User user = new User(userId, "User1", "user1@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_InvalidUserId_ThrowsException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userController.deleteUser(userId));
        verify(userRepository, never()).delete(any(User.class));
    }


    @Test
    void testGetContacts_InvalidUserId_ThrowsException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userController.getContacts(userId));
    }



    @Test
    void testAddContact_InvalidUserId_ReturnsNotFound() {
        Long userId = 1L;
        User contact = new User(2L, "User2", "user2@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = userController.addContact(userId, contact);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepository, never()).save(any(User.class));
    }



    @Test
    void testRemoveContact_ValidUserIdAndContactId_ReturnsOk() {
        Long userId = 1L;
        Long contactId = 2L;
        User user = new User(userId, "User1", "user1@example.com");
        User contact = new User(contactId, "User2", "user2@example.com");
        Set<User> contacts = new HashSet<>();
        contacts.add(contact);
        user.setContacts(contacts);
        contact.setContacts(contacts);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(contactId)).thenReturn(Optional.of(contact));

        ResponseEntity<?> response = userController.removeContact(userId, contactId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(contact);
    }

    @Test
    void testRemoveContact_InvalidUserIdOrContactId_ReturnsNotFound() {
        Long userId = 1L;
        Long contactId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(contactId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.removeContact(userId, contactId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepository, never()).save(any(User.class));
    }
}
