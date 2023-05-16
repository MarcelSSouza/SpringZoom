package com.springzoom.springzoom.Controller;

import java.time.LocalDate;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springzoom.springzoom.Entity.Meeting;
import com.springzoom.springzoom.Entity.User;
import com.springzoom.springzoom.Repository.MeetingRepository;
import com.springzoom.springzoom.Repository.UserRepository;
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
         LocalDate currentDate = LocalDate.now();
        
        System.out.println(currentDate);
        // Retrieve the current and upcoming meetings associated with the user
        List<Meeting> meetings = meetingRepository.findByEmail1OrEmail2AndMeetingDateAfter(email, email, currentDate);
    
        return ResponseEntity.ok(meetings);
    }
    @PutMapping("/meetings/{meetingId}")
    public ResponseEntity<Meeting> updateMeeting(@PathVariable Long meetingId, @Validated @RequestBody Meeting updatedMeeting) {
        // Find the meeting by meeting ID
        Meeting meeting = meetingRepository.findByMeetingId(meetingId);
        
        if (meeting == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Update the meeting properties with the new values
        meeting.setTitle(updatedMeeting.getTitle());
        meeting.setMeetingDate(updatedMeeting.getMeetingDate());
        meeting.setMeetingTime(updatedMeeting.getMeetingTime());
        meeting.setEmail1(updatedMeeting.getEmail1());
        meeting.setEmail2(updatedMeeting.getEmail2());
        
        // Save the updated meeting to the database
        Meeting savedMeeting = meetingRepository.save(meeting);
        
        return ResponseEntity.ok(savedMeeting);
    }
    
    

    
    @DeleteMapping("/meetings/delete/{meetingId}") 
        public ResponseEntity<Meeting> deleteMeeting(@PathVariable Long meetingId) {
            // Find the meeting by meeting ID
            Meeting meeting = meetingRepository.findByMeetingId(meetingId);
        
            if (meeting == null) {
                return ResponseEntity.notFound().build();
            }
        
            // Delete the meeting from the database
            meetingRepository.delete(meeting);
        
            return ResponseEntity.ok(meeting);
        
    }

}
