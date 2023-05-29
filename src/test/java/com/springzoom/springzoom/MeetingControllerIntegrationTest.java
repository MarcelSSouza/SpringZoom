package com.springzoom.springzoom;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.springzoom.springzoom.Controller.MeetingController;
import com.springzoom.springzoom.Entity.Meeting;
import com.springzoom.springzoom.Entity.User;
import com.springzoom.springzoom.Repository.MeetingRepository;
import com.springzoom.springzoom.Repository.UserRepository;

@WebMvcTest(MeetingController.class)
public class MeetingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MeetingRepository meetingRepository;

    private User user1;
    private User user2;
    private Meeting meeting1;
    private Meeting meeting2;

    @BeforeEach
    public void setUp() {
        user1 = new User(1L, "John", "john@example.com", "password", null);
        user2 = new User(2L, "Jane", "jane@example.com", "password", null);

        meeting1 = new Meeting("Meeting 1", LocalDate.now(), LocalTime.now(), user1.getEmail(), user2.getEmail());
        meeting2 = new Meeting("Meeting 2", LocalDate.now(), LocalTime.now(), user1.getEmail(), user2.getEmail());
    }

    @Test
    public void testCreateMeeting() throws Exception {
        BDDMockito.given(userRepository.findByEmail(user1.getEmail())).willReturn(user1);
        BDDMockito.given(userRepository.findByEmail(user2.getEmail())).willReturn(user2);
        BDDMockito.given(meetingRepository.save(BDDMockito.any(Meeting.class))).willReturn(meeting1);

        mockMvc.perform(post("/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Meeting 1\", \"meetingDate\": \"2023-05-29\", \"meetingTime\": \"10:00:00\", \"email1\": \"john@example.com\", \"email2\": \"jane@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Meeting 1")))
                .andExpect(jsonPath("$.email1", is("john@example.com")))
                .andExpect(jsonPath("$.email2", is("jane@example.com")));
    }

    @Test
    public void testGetMeetingsByEmail() throws Exception {
        List<Meeting> meetings = Arrays.asList(meeting1, meeting2);
        BDDMockito.given(userRepository.findByEmail(user1.getEmail())).willReturn(user1);
        BDDMockito.given(meetingRepository.findByEmail1OrEmail2AndMeetingDateAfter(user1.getEmail(), user1.getEmail(), LocalDate.now())).willReturn(meetings);

        mockMvc.perform(get("/meetings/{email}", user1.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Meeting 1")))
                .andExpect(jsonPath("$[1].title", is("Meeting 2")));
    }

    @Test
    public void testUpdateMeeting() throws Exception {
        BDDMockito.given(meetingRepository.findByMeetingId(1L)).willReturn(meeting1);
        BDDMockito.given(meetingRepository.save(BDDMockito.any(Meeting.class))).willReturn(meeting1);

        mockMvc.perform(put("/meetings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Updated Meeting\", \"meetingDate\": \"2023-05-30\", \"meetingTime\": \"12:00:00\", \"email1\": \"john@example.com\", \"email2\": \"jane@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Meeting")))
                .andExpect(jsonPath("$.email1", is("john@example.com")))
                .andExpect(jsonPath("$.email2", is("jane@example.com")));
    }

    @Test
    public void testDeleteMeeting_ValidMeetingId_ReturnsDeletedMeeting() throws Exception {
        // Mock repository to return the meeting
        BDDMockito.given(meetingRepository.findByMeetingId(1L)).willReturn(meeting1);

        mockMvc.perform(delete("/meetings/delete/{meetingId}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteMeeting_InvalidMeetingId_ReturnsNotFound() throws Exception {
        // Mock repository to return null
        BDDMockito.given(meetingRepository.findByMeetingId(100L)).willReturn(null);

        mockMvc.perform(delete("/meetings/delete/{meetingId}", 100L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMeeting_ValidMeetingId_DeletesMeetingFromRepository() throws Exception {
        // Mock repository to return the meeting
        BDDMockito.given(meetingRepository.findByMeetingId(1L)).willReturn(meeting1);

        mockMvc.perform(delete("/meetings/delete/{meetingId}", 1L))
                .andExpect(status().isOk());

        BDDMockito.verify(meetingRepository, BDDMockito.times(1)).delete(meeting1);
    }

    @Test
    public void testCreateMeeting_InvalidUserEmail_ReturnsNotFound() throws Exception {
        // Mock repository to return null for one of the user emails
        BDDMockito.given(userRepository.findByEmail(user1.getEmail())).willReturn(user1);
        BDDMockito.given(userRepository.findByEmail(user2.getEmail())).willReturn(null);

        mockMvc.perform(post("/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Meeting 1\", \"meetingDate\": \"2023-05-29\", \"meetingTime\": \"10:00:00\", \"email1\": \"john@example.com\", \"email2\": \"jane@example.com\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateMeeting_ValidUserEmails_ReturnsCreatedMeeting() throws Exception {
        // Mock repository to return the users
        BDDMockito.given(userRepository.findByEmail(user1.getEmail())).willReturn(user1);
        BDDMockito.given(userRepository.findByEmail(user2.getEmail())).willReturn(user2);

        // Mock repository to return the saved meeting
        BDDMockito.given(meetingRepository.save(BDDMockito.any(Meeting.class))).willReturn(meeting1);

        mockMvc.perform(post("/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Meeting 1\", \"meetingDate\": \"2023-05-29\", \"meetingTime\": \"10:00:00\", \"email1\": \"john@example.com\", \"email2\": \"jane@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Meeting 1")))
                .andExpect(jsonPath("$.email1", is("john@example.com")))
                .andExpect(jsonPath("$.email2", is("jane@example.com")));
    }

    @Test
    public void testGetMeetingsByEmail_InvalidUserEmail_ReturnsNotFound() throws Exception {
        // Mock repository to return null for the user email
        BDDMockito.given(userRepository.findByEmail(user1.getEmail())).willReturn(null);
    
        mockMvc.perform(get("/meetings/{email}", "john@example.com"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testGetMeetingsByEmail_ValidUserEmail_ReturnsMeetings() throws Exception {
        // Mock repository to return the user
        BDDMockito.given(userRepository.findByEmail(user1.getEmail())).willReturn(user1);
    
        // Mock repository to return the list of meetings
        List<Meeting> meetings = Arrays.asList(meeting1, meeting2);
        BDDMockito.given(meetingRepository.findByEmail1OrEmail2AndMeetingDateAfter(user1.getEmail(), user1.getEmail(), LocalDate.now())).willReturn(meetings);
    
        mockMvc.perform(get("/meetings/{email}", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Meeting 1")))
                .andExpect(jsonPath("$[1].title", is("Meeting 2")));
    }
    



}
