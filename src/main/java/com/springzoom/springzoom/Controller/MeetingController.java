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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springzoom.springzoom.Entity.Meeting;
import com.springzoom.springzoom.Entity.User;
import com.springzoom.springzoom.Repository.MeetingRepository;
import com.springzoom.springzoom.Repository.UserRepository;

@RestController
public class MeetingController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MeetingRepository meetingRepository;
    
   @PostMapping("/meetings")
public ResponseEntity<?> createMeeting( @RequestBody Meeting meeting) {
    // Get user1 and user2 by email
    User optionalUser1 = userRepository.findByEmail(meeting.getEmail1());
    User optionalUser2 = userRepository.findByEmail(meeting.getEmail2());
        
    // If both users exist, create the meeting


        
        // Set the users and meeting ID for the meeting
        meeting.setEmail1(optionalUser1.getEmail());
        meeting.setEmail2(optionalUser2.getEmail());
        meeting.setMeetingId(generateRandomNumber());
        
        // Save the meeting to the database
        meetingRepository.save(meeting);
        
        return new ResponseEntity<>(meeting, HttpStatus.CREATED);

}

    
    // Helper method to generate a random 6-digit number for the meeting ID
    private int generateRandomNumber() {
        return (int)(Math.random() * (999999 - 100000 + 1) + 100000);
    }
}
