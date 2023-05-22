import React, { useState, useEffect } from "react";
import Container from "react-bootstrap/Container";
import Table from "react-bootstrap/Table";
import Card from "react-bootstrap/Card";
import axios from "axios";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import Form from "react-bootstrap/Form";
import LogoutButton from "./LogoutButton";

function Home() {
  const [meetings, setMeetings] = useState([]);
  const [selectedMeeting, setSelectedMeeting] = useState(null);
  const storedUser = sessionStorage.getItem("user");
  const user = storedUser ? JSON.parse(storedUser) : null;
  const [editingMeeting, setEditingMeeting] = useState(null);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  useEffect(() => {
    if (!!user) {
      fetchMeetingsByEmail(user.email);
    }
  }, []);

  const fetchMeetingsByEmail = (email) => {
    axios
      .get(`http://localhost:8080/meetings/${email}`)
      .then((response) => {
        setMeetings(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  };

  const handleEditClick = (meeting) => {
    setEditingMeeting(meeting);
    setSelectedMeeting(null);
  };

  const handleSaveChanges = () => {
    axios
      .put(`http://localhost:8080/meetings/${editingMeeting.meetingId}`, editingMeeting)
      .then((response) => {
        alert("Meeting updated successfully!");
        setEditingMeeting(null);
        fetchMeetingsByEmail(user.email);
      })
      .catch((error) => {
        alert("Failed to update meeting: " + error.response.data.message);
      });
  };

  const handleDetailsClick = (meeting) => {
    setSelectedMeeting(meeting);
    setEditingMeeting(null);
  };

  const handleCloseCard = () => {
    setSelectedMeeting(null);
  };

  const filterMeetings = () => {
    const filtered = meetings.filter((meeting) => {
      const meetingDateTime = new Date(meeting.meetingDate + " " + meeting.meetingTime);
      const startDateTime = new Date(startDate);
      const endDateTime = new Date(endDate);

      return meetingDateTime >= startDateTime && meetingDateTime <= endDateTime;
    });

    setMeetings(filtered);
  };

  return (
    <>
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="/home">SpringZomm</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link href="/home">Home</Nav.Link>
              <Nav.Link href="/contacts">Contacts</Nav.Link>
              <Nav.Link href="/newmeeting">Add Meeting</Nav.Link>
              <LogoutButton />
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      <Container>
        <h2>Welcome to SpringZoom {user.name}</h2>
        <h2>Your id is {user.id}</h2>
        <h3>
          SpringZoom is a web application that allows you to schedule meetings with your contacts.
        </h3>
        <Form>
          <Form.Group className="mb-3" controlId="formBasicStartDate">
            <Form.Label>Start Date</Form.Label>
            <Form.Control
              type="date"
              placeholder="Start Date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formBasicEndDate">
            <Form.Label>End Date</Form.Label>
            <Form.Control
              type="date"
              placeholder="End Date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </Form.Group>
          <button type="button" onClick={filterMeetings}>
            Filter
          </button>
        </Form>
        <Table striped bordered>
          <thead>
            <tr>
              <th>Meeting ID</th>
              <th>Title</th>
              <th>Email 1</th>
              <th>Email 2</th>
              <th>Date</th>
              <th>Time</th>
              <th>Delete</th>
              <th>Edit</th>
              <th>Details</th>
            </tr>
          </thead>
          <tbody>
            {meetings.map((meeting) => (
              <tr key={meeting.meetingId}>
                <td>{meeting.meetingId}</td>
                <td>{meeting.title}</td>
                <td>{meeting.email1}</td>
                <td>{meeting.email2}</td>
                <td>{meeting.meetingDate}</td>
                <td>{meeting.meetingTime}</td>
                <td>
                  <button
                    onClick={() => {
                      axios
                        .delete(`http://localhost:8080/meetings/delete/${meeting.meetingId}`)
                        .then((response) => {
                          alert("Meeting deleted successfully!");
                          fetchMeetingsByEmail(user.email);
                        })
                        .catch((error) => {
                          alert("Failed to delete meeting: " + error.response.data.message);
                        });
                    }}
                  >
                    Delete Meeting
                  </button>
                </td>
                <td>
                  <button onClick={() => handleEditClick(meeting)}>Edit</button>
                </td>
                <td>
                  <button onClick={() => handleDetailsClick(meeting)}>Details</button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
        {selectedMeeting && (
          <Card>
            <Card.Header>{selectedMeeting.meetingId}</Card.Header>
            <Card.Body>
              <Card.Title>Meeting Details</Card.Title>
              <Card.Text>
                <strong>Title:</strong> {selectedMeeting.title}
                <br />
                <strong>Email 1:</strong> {selectedMeeting.email1}
                <br />
                <strong>Email 2:</strong> {selectedMeeting.email2}
                <br />
                <strong>Date:</strong> {selectedMeeting.meetingDate}
                <br />
                <strong>Time:</strong> {selectedMeeting.meetingTime}
                <br />
              </Card.Text>
              <button onClick={handleCloseCard}>Close</button>
            </Card.Body>
          </Card>
        )}
        {editingMeeting && (
          <Card>
            <Card.Header>Edit Meeting</Card.Header>
            <Card.Body>
              <Form className="d-flex flex-column">
                <Form.Group className="mb-3" controlId="formBasicTitle">
                  <Form.Label>Title</Form.Label>
                  <Form.Control
                    type="title"
                    placeholder="Title"
                    value={editingMeeting.title}
                    onChange={(e) =>
                      setEditingMeeting({ ...editingMeeting, title: e.target.value })
                    }
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicDate">
                  <Form.Label>Date</Form.Label>
                  <Form.Control
                    type="date"
                    placeholder="Date"
                    value={editingMeeting.meetingDate}
                    onChange={(e) =>
                      setEditingMeeting({ ...editingMeeting, meetingDate: e.target.value })
                    }
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicHour">
                  <Form.Label>Hour</Form.Label>
                  <Form.Control
                    type="time"
                    placeholder="Hour"
                    value={editingMeeting.meetingTime}
                    onChange={(e) =>
                      setEditingMeeting({ ...editingMeeting, meetingTime: e.target.value })
                    }
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                  <Form.Label>Your email</Form.Label>
                  <Form.Control
                    type="email"
                    placeholder="Enter email"
                    value={editingMeeting.email1}
                    onChange={(e) =>
                      setEditingMeeting({ ...editingMeeting, email1: e.target.value })
                    }
                  />
                  <Form.Text className="text-muted"></Form.Text>
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                  <Form.Label>Email of the other user</Form.Label>
                  <Form.Control
                    type="email"
                    placeholder="Enter email"
                    value={editingMeeting.email2}
                    onChange={(e) =>
                      setEditingMeeting({ ...editingMeeting, email2: e.target.value })
                    }
                  />
                  <Form.Text className="text-muted"></Form.Text>
                </Form.Group>
                <button onClick={handleSaveChanges}>Save Changes</button>
              </Form>
            </Card.Body>
          </Card>
        )}
      </Container>
    </>
  );
}

export default Home;
