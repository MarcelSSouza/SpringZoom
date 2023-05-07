package com.springzoom.springzoom.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.springzoom.springzoom.Entity.User;
import com.springzoom.springzoom.Repository.UserRepository;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow();
    }

    @PostMapping("")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User loginUser) {
        User user = userRepository.findByEmailAndPassword(loginUser.getEmail(), loginUser.getPassword());

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow();

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setContacts(userDetails.getContacts());

        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow();

        userRepository.delete(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/contacts")
    public List<User> getContacts(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow();

        return user.getContacts();
    }

    @PostMapping("/{id}/contacts/{contactId}")
    public User addContact(@PathVariable Long id, @PathVariable Long contactId) {
        User user = userRepository.findById(id)
                .orElseThrow();

        User contact = userRepository.findById(contactId)
                .orElseThrow();

        user.getContacts().add(contact);
        contact.getContacts().add(user);

        userRepository.save(user);
        userRepository.save(contact);

        return user;
    }

    @PostMapping("/{id}/add-contact")
    public User addContactToUser(@PathVariable Long id, @RequestBody User contact) {
        User user = userRepository.findById(id)
                .orElseThrow();

                contact = userRepository.findByEmail(contact.getEmail());

                if (contact == null) {
                    // handle case where user with specified email is not found
                    return null;
                }
                
                if (user.getContacts().contains(contact) || user.getId().equals(contact.getId())) {
                    // handle case where contact already exists or is the same as the user
                    return null;
                }
                
                user.getContacts().add(contact);
                contact.getContacts().add(user);
                
                userRepository.save(user);
                userRepository.save(contact);
                
                return user;
    }

    @DeleteMapping("/{id}/contacts/{contactId}")
    public User removeContact(@PathVariable Long id, @PathVariable Long contactId) {
        User user = userRepository.findById(id)
                .orElseThrow();

        User contact = userRepository.findById(contactId)
                .orElseThrow();

        user.getContacts().remove(contact);
        contact.getContacts().remove(user);

        userRepository.save(user);
        userRepository.save(contact);

        return user;
    }
}
