package com.springzoom.springzoom;

import com.springzoom.springzoom.Controller.MeetingController;
import com.springzoom.springzoom.Entity.Meeting;
import com.springzoom.springzoom.Entity.User;
import com.springzoom.springzoom.Repository.MeetingRepository;
import com.springzoom.springzoom.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MeetingControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private MeetingController meetingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMeeting_ValidUsers_ReturnsCreatedMeeting() {
        User user1 = new User(1L, "User1", "user1@example.com");
        User user2 = new User(2L, "User2", "user2@example.com");
        Meeting meeting = new Meeting();
        meeting.setEmail1(user1.getEmail());
        meeting.setEmail2(user2.getEmail());

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        when(userRepository.findByEmail(user2.getEmail())).thenReturn(user2);
        when(meetingRepository.save(meeting)).thenReturn(meeting);

        ResponseEntity<Meeting> response = meetingController.createMeeting(meeting);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(meeting, response.getBody());
        verify(meetingRepository, times(1)).save(meeting);
    }
    @Test
    void testCreateMeeting_InvalidUser_ReturnsNotFound() {
        User user1 = new User(1L, "User1", "user1@example.com");
        User user2 = new User(2L, "User2", "user2@example.com");
        Meeting meeting = new Meeting();
        meeting.setEmail1(user1.getEmail());
        meeting.setEmail2(user2.getEmail());

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(null);
        when(userRepository.findByEmail(user2.getEmail())).thenReturn(user2);

        ResponseEntity<Meeting> response = meetingController.createMeeting(meeting);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(meetingRepository, never()).save(any(Meeting.class));
    }
    @Test
    void testGetMeetingsByEmail_ValidEmail_ReturnsMeetings() {
        String email = "user1@example.com";
        User user = new User(1L, "User1", email);
        Meeting meeting1 = new Meeting();
        Meeting meeting2 = new Meeting();
        List<Meeting> meetings = new ArrayList<>();
        meetings.add(meeting1);
        meetings.add(meeting2);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(meetingRepository.findByEmail1OrEmail2AndMeetingDateAfter(email, email, LocalDate.now()))
                .thenReturn(meetings);

        ResponseEntity<List<Meeting>> response = meetingController.getMeetingsByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(meetings, response.getBody());
    }
    @Test
    void testUpdateMeeting_ValidMeetingId_ReturnsUpdatedMeeting() {
        Long meetingId = 1L;
        Meeting existingMeeting = new Meeting();
        Meeting updatedMeeting = new Meeting();
        
        when(meetingRepository.findByMeetingId(meetingId)).thenReturn(existingMeeting);
        when(meetingRepository.save(existingMeeting)).thenReturn(existingMeeting);

        ResponseEntity<Meeting> response = meetingController.updateMeeting(meetingId, updatedMeeting);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingMeeting, response.getBody());
        verify(meetingRepository, times(1)).save(existingMeeting);
    }

    @Test
    void testUpdateMeeting_InvalidMeetingId_ReturnsNotFound() {
        Long meetingId = 1L;
        Meeting updatedMeeting = new Meeting();
        
        when(meetingRepository.findByMeetingId(meetingId)).thenReturn(null);

        ResponseEntity<Meeting> response = meetingController.updateMeeting(meetingId, updatedMeeting);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(meetingRepository, never()).save(any(Meeting.class));
    }

    @Test
    void testDeleteMeeting_ValidMeetingId_ReturnsDeletedMeeting() {
        Long meetingId = 1L;
        Meeting meeting = new Meeting();
        
        when(meetingRepository.findByMeetingId(meetingId)).thenReturn(meeting);

        ResponseEntity<Meeting> response = meetingController.deleteMeeting(meetingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(meeting, response.getBody());
        verify(meetingRepository, times(1)).delete(meeting);
    }

    @Test
    void testDeleteMeeting_InvalidMeetingId_ReturnsNotFound() {
        Long meetingId = 1L;
        
        when(meetingRepository.findByMeetingId(meetingId)).thenReturn(null);

        ResponseEntity<Meeting> response = meetingController.deleteMeeting(meetingId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(meetingRepository, never()).delete(any(Meeting.class));
    }
}
