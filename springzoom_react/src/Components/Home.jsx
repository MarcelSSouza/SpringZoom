import React, { useState, useEffect } from "react";
import Container from 'react-bootstrap/Container';
import Table from 'react-bootstrap/Table';
import axios from 'axios';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';


function Home() {
  const [meetings, setMeetings] = useState([]);
  const storedUser = sessionStorage.getItem('user');
  const user = storedUser ? JSON.parse(storedUser) : null;
  
  useEffect(() => {
    if (user) {
      fetchMeetingsByEmail(user.email);
    }
  }, [user]);

  const fetchMeetingsByEmail = (email) => {
    axios.get(`http://localhost:8080/meetings/${email}`)
      .then(response => {
        setMeetings(response.data);
      })
      .catch(error => {
        console.error(error);
      });
  };

  return (
    <>
          <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="#home">SpringZomm</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link href="/home">Home</Nav.Link>
              <Nav.Link href="/contacts">Contacts</Nav.Link>
              <Nav.Link href="/newmeeting">Add Meeting</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      <Container>
        <h2>Welcome to SpringZoom {user.name}</h2>
        <h2>Your id is {user.id}</h2>
        <h3>SpringZoom is a web application that allows you to schedule meetings with your contacts.</h3>
        <Table striped bordered>
          <thead>
            <tr>
              <th>Meeting ID</th>
              <th>Email 1</th>
              <th>Email 2</th>
              {/* Add additional columns as needed */}
            </tr>
          </thead>
          <tbody>
            {meetings.map(meeting => (
              <tr key={meeting.meetingId}>
                <td>{meeting.meetingId}</td>
                <td>{meeting.email1}</td>
                <td>{meeting.email2}</td>
                <td><button onClick={() => { axios.delete(`http://localhost:8080/meetings/delete/${meeting.meetingId}`).then((response) => {
                  alert("Meeting deleted successfully!");
                  fetchMeetingsByEmail(user.email);
                }).catch((error) => {
                  alert("Failed to delete meeting: " + error.response.data.message);
                }); }}>Delete Meeting</button></td>
                
                {/* Add additional columns as needed */}
              </tr>
            ))}
          </tbody>
        </Table>
      </Container>
    </>
  );
}

export default Home;
