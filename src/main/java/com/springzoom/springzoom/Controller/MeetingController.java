package com.springzoom.springzoom.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springzoom.springzoom.Entity.Meeting;
import com.springzoom.springzoom.Entity.User;
import com.springzoom.springzoom.Repository.MeetingRepository;
import com.springzoom.springzoom.Repository.UserRepository;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class MeetingController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MeetingRepository meetingRepository;
    
    @PostMapping("/meetings")
    public ResponseEntity<Meeting> createMeeting(@Validated @RequestBody Meeting meeting) {
        // Get user1 and user2 by email
        User user1 = userRepository.findByEmail(meeting.getEmail1());
        User user2 = userRepository.findByEmail(meeting.getEmail2());
    
        // If both users exist, create the meeting
        if (user1 == null || user2 == null) {
            return ResponseEntity.notFound().build();
        }
    
        // Set the users and meeting ID for the meeting
        meeting.setEmail1(user1.getEmail());
        meeting.setEmail2(user2.getEmail());
        meeting.setMeetingId(generateRandomNumber());
    
        // Save the meeting to the database
        meetingRepository.save(meeting);
    
        return new ResponseEntity<>(meeting, HttpStatus.CREATED);
    }
    
    
    // Helper method to generate a random 6-digit number for the meeting ID
    private int generateRandomNumber() {
        return (int)(Math.random() * (999999 - 100000 + 1) + 100000);
    }

    @GetMapping("/meetings/{email}")
public ResponseEntity<List<Meeting>> getMeetingsByEmail(@PathVariable String email) {
    // Find the user by email
    User user = userRepository.findByEmail(email);

    if (user == null) {
        return ResponseEntity.notFound().build();
    }

    // Retrieve the meetings associated with the user
    List<Meeting> meetings = meetingRepository.findByEmail1OrEmail2(email, email);

    return ResponseEntity.ok(meetings);
}

}
