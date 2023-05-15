package com.springzoom.springzoom.Repository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springzoom.springzoom.Entity.Meeting;
import com.springzoom.springzoom.Entity.User;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByEmail1OrEmail2(String email, String email2);

    Meeting findByMeetingId(Long meetingId);

    List<Meeting> findByEmail1OrEmail2AndMeetingDateAfter(String email, String email2, LocalDate formattedDate);
}
